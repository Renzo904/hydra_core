/*
 * Created by @UnbarredStream on 12/06/22 18:41
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 12/06/22 18:41
 */

package knf.hydra.module.cuevana.retrofit

import knf.hydra.core.models.BypassModel
import knf.hydra.core.models.InfoModel
import knf.hydra.core.models.data.ReviewResult
import knf.hydra.module.cuevana.models.CuevanaDirectoryModel
import knf.hydra.module.cuevana.models.CuevanaRecentModel
import knf.hydra.module.cuevana.models.CuevanaSearchModel
import okhttp3.OkHttpClient
import org.json.JSONObject
import pl.droidsonroids.retrofit2.JspoonConverterFactory
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

object NetworkRepository {
    val defaultCookies = mapOf("device" to "Computer")
    lateinit var currentBypass: BypassModel

    private val factory by lazy {
        Retrofit.Builder()
            .baseUrl("https://ww3.cuevana3.me/")
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(JspoonConverterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .build().create(NetworkFactory::class.java)
    }

    

    fun getDirectoryPage(page: Int, bypassModel: BypassModel): List<CuevanaDirectoryModel> {
        currentBypass = bypassModel
        return factory.getDirectoryPage(page, bypassModel.asMap(defaultCookies)).execute().body()!!.list
    }

    fun getSearchPage(query: String?, page: Int, bypassModel: BypassModel): List<CuevanaSearchModel> {
        currentBypass = bypassModel
        return factory.getSearchPage(page, query?:"", bypassModel.asMap(defaultCookies)).execute().body()!!.list
    }

    fun getInfo(url: String, bypassModel: BypassModel): InfoModel?{
        currentBypass = bypassModel
        return try {
            factory.getInfo(url,bypassModel.asMap(defaultCookies)).execute().body()
        }catch (e:Exception){
            e.printStackTrace()
            null
        }
    }

    fun getRecentPage(bypassModel: BypassModel): List<CuevanaRecentModel> {
        currentBypass = bypassModel
        return factory.getRecentPage(bypassModel.asMap(defaultCookies)).execute().body()!!.list
    }

    fun getFembedUrl(id: String, bypassModel: BypassModel): String {
        return try {
            JSONObject(factory.getFembedUrl(id, bypassModel.asMap(defaultCookies)).execute().body()!!).getString("url").replace("\\", "")
        }catch (e:Exception){
            e.printStackTrace()
            ""
        }
    }

    fun getApiAlfaUrl(code: String, url: String): String {
        return try {
            factory.getApiAlfaUrl(code, url).execute().raw().request.url.toString()
        }catch (e:Exception){
            e.printStackTrace()
            ""
        }
    }
}