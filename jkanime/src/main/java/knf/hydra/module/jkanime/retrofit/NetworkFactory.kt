package knf.hydra.module.jkanime.retrofit

import knf.hydra.module.jkanime.models.*
import retrofit2.Call
import retrofit2.http.*

interface NetworkFactory {
    @GET("/directorio/{page}")
    fun getDirectoryPage(@Path(value = "page") page: Int, @HeaderMap bypass: Map<String,String>): Call<DirectoryPage>

    @GET("/buscar/{query}/{page}")
    fun getSearchPage(@Path(value = "query") query: String, @Path(value = "page") page: Int, @HeaderMap bypass: Map<String,String>): Call<SearchPage>
}