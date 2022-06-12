package knf.hydra.module.jkanime.retrofit

import knf.hydra.core.models.BypassModel
import knf.hydra.module.jkanime.models.JkDirectoryModel
import knf.hydra.module.jkanime.models.JkSearchModel
import okhttp3.OkHttpClient
import pl.droidsonroids.retrofit2.JspoonConverterFactory
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

object NetworkRepository {
    val defaultCookies = mapOf("device" to "Computer")
    lateinit var currentBypass: BypassModel

    private val factory by lazy {
        Retrofit.Builder()
            .baseUrl("https://jkanime.net")
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(JspoonConverterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .build().create(NetworkFactory::class.java)
    }

    

    fun getDirectoryPage(page: Int, bypassModel: BypassModel): List<JkDirectoryModel> {
        currentBypass = bypassModel
        return factory.getDirectoryPage(page, bypassModel.asMap(defaultCookies)).execute().body()!!.list
    }

    fun getSearchPage(query: String?, page: Int, bypassModel: BypassModel): List<JkSearchModel> {
        currentBypass = bypassModel
        return factory.getSearchPage(query?:"", page, bypassModel.asMap(defaultCookies)).execute().body()!!.list
    }
}