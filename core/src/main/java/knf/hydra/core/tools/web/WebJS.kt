package knf.hydra.core.tools.web

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.webkit.CookieManager
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.Keep

class WebJS(context: Context) {
    private val webView = WebView(context)
    val defaultUserAgent: String = webView.settings.userAgentString
    private var callback: ((String) -> Unit)? = null

    init {
        webView.settings.apply {
            javaScriptEnabled = true
        }
        webView.addJavascriptInterface(JSInterface{ callback?.invoke(it) },"myInterface")
    }

    fun evalOnFinish(link: String, userAgent :String, timeout: Long, js: String, callback: (String) -> Unit){
        this.callback = callback
        val handler = Handler(Looper.getMainLooper())
        val runnable = {
            webView.loadUrl("javascript:myInterface.returnResult(eval('try{$js}catch(e){e}'));")
        }
        webView.settings.userAgentString = userAgent
        webView.settings.blockNetworkImage = true
        webView.webViewClient = object : WebViewClient(){
            override fun onPageFinished(view: WebView?, url: String?) {
                handler.removeCallbacks(runnable)
                handler.postDelayed(runnable,timeout)
            }
        }
        webView.loadUrl(link)
    }

    fun cookiesOnFinish(link: String, userAgent: String, timeout: Long, cookies: (String) -> Unit) {
        val handler = Handler(Looper.getMainLooper())
        val callback = {
            cookies(CookieManager.getInstance().getCookie(link))
        }
        webView.settings.userAgentString = userAgent
        webView.settings.blockNetworkImage = true
        webView.webViewClient = object : WebViewClient(){
            override fun onPageFinished(view: WebView?, url: String?) {
                handler.removeCallbacks(callback)
                handler.postDelayed(callback,timeout)
            }
        }
        webView.loadUrl(link)
    }

    fun evalJs(code: String, result: (String) -> Unit) {
        webView.evaluateJavascript(code, result)
    }

    @Keep
    class JSInterface(private val callback: (String) -> Unit){
        @JavascriptInterface
        fun returnResult(result: String){
            callback(result)
        }
    }
}