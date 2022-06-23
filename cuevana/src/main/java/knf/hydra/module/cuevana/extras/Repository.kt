/*
 * Created by @UnbarredStream on 12/06/22 18:40
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 12/06/22 12:39
 */

package knf.hydra.module.cuevana.extras

import android.provider.MediaStore
import android.util.Log
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import org.json.JSONObject
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
        Log.d("SOURCEDATADEBUG", content.link)
        Log.d("SOURCEDATADEBUG", "Llego al sourcedata")
        return VideoSource(
            flow {
                val asd = VideoItem("Zippyshare", "https://embedsito.com/v/136xyaj7q6p8kn1", quality = VideoItem.Quality.MULTIPLE, canDownload = true, type = "SUB")
                val asdd = VideoItem("Zippysharde", "https://embedsito.com/v/136xyaj7q6p8kn1", quality = VideoItem.Quality.MULTIPLE, canDownload = true, type = "SUB")
                val asddd = VideoItem("Zippysharse", "https://embedsito.com/v/136xyaj7q6p8kn1", quality = VideoItem.Quality.MULTIPLE, canDownload = true, type = "SUB")
                val items = mutableListOf<VideoItem>()
                
                for(i in 1..10){
                    items.add(
                        VideoItem(
                            "Test$i",
                            "https://embedsito.com/v/136xyaj7q6p8kn1",
                            quality = VideoItem.Quality.MULTIPLE,
                            canDownload = i%2==0,
                            type = "TEST"
                        )

                    )
                }
                //items.add(asd)
                //items.add(asdd)
                //items.add(asddd)

                items.forEach {
                    Log.d("SOURCEDATADEBUG", it.name)
                    Log.d("SOURCEDATADEBUG", it.link)
                    Log.d("SOURCEDATADEBUG", if (it.canDownload) "True" else "False")
                    Log.d("SOURCEDATADEBUG", it.quality.toString())
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
            pagingSourceFactory = { SearchSource(query, bypassModel, filters) }
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
            pagingSourceFactory = { DirectorySource(bypassModel, filters) }
        ).flow
    }

}