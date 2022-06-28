/*
 * Created by @UnbarredStream on 12/06/22 18:40
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 12/06/22 12:39
 */

package knf.hydra.module.cuevana.extras

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import knf.hydra.core.HeadRepository
import knf.hydra.core.models.*
import knf.hydra.core.models.data.FilterRequest
import knf.hydra.core.models.data.SourceData
import knf.hydra.core.models.data.VideoItem
import knf.hydra.core.models.data.VideoSource
import knf.hydra.module.cuevana.repository.DirectorySource
import knf.hydra.module.cuevana.repository.RecentsSource
import knf.hydra.module.cuevana.repository.SearchSource
import knf.hydra.module.cuevana.retrofit.NetworkRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

class Repository : HeadRepository(){

    override fun infoPage(
        link: String,
        bypassModel: BypassModel
    ): Flow<InfoModel?> {
        return flow {
            emit(withContext(Dispatchers.IO) {
                NetworkRepository.getInfo(link, bypassModel)
            })
        }
    }

    override suspend fun sourceData(content: ContentItemMin, bypassModel: BypassModel): SourceData<*>{
        return VideoSource(
            flow {
                val items = mutableListOf<VideoItem>()
                val doc = Jsoup.connect(content.link).headers(bypassModel.asMap(NetworkRepository.defaultCookies)).get()
                val iframes = doc.select("div.TPlayerTb")
                try {
                    iframes.let { elements ->
                        elements.forEach { element -> run {
                            val iframe = element.selectFirst("iframe")
                            val type = if(element.id().contains("OptL")) "LAT" else "SUB"
                            val link = iframe.attr("data-src")
                            if(
                                link.length < 400 &&
                                !link.contains("fembed")
                            ){
                                return@forEach
                            }



                            if(iframe != null){
                                val code = link.substringAfterLast("?h=")
                                when {
                                    link.contains("fembed") -> {
                                        val videoLink = withContext(Dispatchers.IO){
                                            NetworkRepository.getFembedUrl(code, bypassModel)
                                        }
                                        items.add(
                                            VideoItem(
                                                "Fembed",
                                                videoLink,
                                                quality = VideoItem.Quality.MULTIPLE,
                                                canDownload = true,
                                                type = type
                                            )
                                        )
                                    }

                                    link.contains("apialfa.tomatomatela") -> {
                                        val codeTwo = withContext(Dispatchers.IO) {
                                            NetworkRepository.getApiAlfaUrl(
                                                url = "rd.php",
                                                code = code
                                            )
                                        }

                                        if(codeTwo != ""){
                                            val videoLink = withContext(Dispatchers.IO) {
                                                NetworkRepository.getApiAlfaUrl(
                                                    url = "redirect_ddh.php",
                                                    code = codeTwo.substringAfterLast("?h=")
                                                )
                                            }
                                            if(videoLink.contains("tomatomatela")){
                                                items.add(
                                                    VideoItem(
                                                        "Tomatomatela",
                                                        videoLink,
                                                        quality = VideoItem.Quality.MEDIUM,
                                                        canDownload = true,
                                                        type = type
                                                    )
                                                )
                                            }
                                        }
                                    }

                                    else -> {}

                                }
                            }
                        }
                        }

                    }
                } catch (e:Exception) {
                    e.printStackTrace()
                }
                emit(items)
            }
        )
    }

    override suspend fun recentsPager(bypassModel: BypassModel): Flow<PagingData<RecentModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = 15,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { RecentsSource(bypassModel) }
        ).flow
    }

    override suspend fun searchPager(
        query: String?,
        bypassModel: BypassModel,
        filters: FilterRequest?
    ): Flow<PagingData<DirectoryModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = 45,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { SearchSource(query, bypassModel) }
        ).flow
    }

    override suspend fun directoryPager(
        bypassModel: BypassModel,
        filters: FilterRequest?
    ): Flow<PagingData<DirectoryModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = 45,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { DirectorySource(bypassModel) }
        ).flow
    }

}