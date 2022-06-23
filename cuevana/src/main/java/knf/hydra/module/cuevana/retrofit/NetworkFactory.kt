/*
 * Created by @UnbarredStream on 12/06/22 18:41
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 12/06/22 18:41
 */

package knf.hydra.module.cuevana.retrofit

import knf.hydra.module.cuevana.models.*
import retrofit2.Call
import retrofit2.http.*

interface NetworkFactory {
    @GET("/peliculas/page/{page}/")
    fun getDirectoryPage(@Path(value = "page") page: Int, @HeaderMap bypass: Map<String,String>): Call<DirectoryPage>

    @GET
    fun getInfo(@Url url: String, @HeaderMap bypass: Map<String,String>): Call<CuevanaMovieInfo>

    @GET("/serie")
    fun getRecentPage(@HeaderMap bypass: Map<String,String>): Call<RecentsPage>

    @GET("/page/{page}/")
    fun getSearchPage(@Path(value = "page") page: Int, @Query("s") query: String, @HeaderMap bypass: Map<String,String>): Call<SearchPage>
}