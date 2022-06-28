package knf.hydra.module.cuevana.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import knf.hydra.core.models.ContentItemModel
import knf.hydra.module.cuevana.models.CuevanaMovieInfo
import knf.hydra.module.cuevana.models.CuevanaContentItemModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChaptersSource (private  val constructor: CuevanaMovieInfo.ChapterConstructor) : PagingSource<Int, ContentItemModel>() {
    override fun getRefreshKey(state: PagingState<Int, ContentItemModel>): Int? {
        return state.anchorPosition
    }

    /*
    private fun getCapThumbLink(id: String, temp: Int?, cap: Int?): String?{
        return try {
            //URL("https://api.themoviedb.org/3/search/tv?api_key=$tmbdApiKey&query=$id").readText()
            val api = Uri.Builder()
                .scheme("https")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendQueryParameter("api_key", tmbdApiKey)
            val searchResults = JSONObject(URL(
                api
                    .appendPath("search")
                    .appendPath("tv")
                    .appendQueryParameter("query", id)
                    .build().toString()
            ).readText())

            val seriesId = searchResults.getJSONArray("results").getJSONObject(0).getInt("id")
            val thumbResults = JSONObject(URL(
                api
                    .appendPath("tv")
                    .appendPath(seriesId.toString())
                    .appendPath("season")
                    .appendPath(temp.toString())
                    .appendPath("episode")
                    .appendPath(cap.toString())
                    .appendPath("images")
                    .build().toString()
            ).readText())
            val thumbLink = thumbResults.getJSONArray("stills").getJSONObject(0).getString("file_path")
            return "https://image.tmdb.org/t/p/w500$thumbLink"
        } catch (e:Exception){
            e.printStackTrace()
            null
        }
    }
    */

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ContentItemModel> {
        return try {
            val list = withContext(Dispatchers.IO){
                constructor.chapterList.subList((params.key?:0)*10,constructor.chapterList.size).take(10)
                    .map {
                        var number = 0.0
                        if(it.tempNumber != null && it.chapNumber != null){
                            number = ((it.tempNumber * 10000) + it.chapNumber).toDouble()
                        }
                        CuevanaContentItemModel(
                            constructor.seriesId,
                            it.chapterLink,
                            number,
                            if (it.thumbLink.contains("https:")) it.thumbLink else "https:${it.thumbLink}"
                        )
                    }
            }
            LoadResult.Page(
                list,
                null,
                if (list.size < 10) null else (params.key?:0) + 1
            )
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }
}