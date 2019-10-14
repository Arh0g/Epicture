package com.epitech.epicture

import android.app.ProgressDialog
import android.os.Bundle
import android.os.PersistableBundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.epitech.epicture.R

class LoginActivity : AppCompatActivity() {

    public var requestUrl = "https://api.imgur.com/oauth2/authorize" +
            "?client_id=979976772a2d967" +
            "&response_type=token" +
            "&state=APPLICATION_STATE"

    fun parseImgurCallback(url: String) {
        //Parse URL
        var parseUrl: Array<String> = url.split("#".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()[1].split("&".toRegex())
            .dropLastWhile { it.isEmpty() }
            .toTypedArray()

        //Fill Imgur Class
        imgurClient.accessToken = parseUrl[0].split("=".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()[1]
        imgurClient.refreshToken = parseUrl[3].split("=".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()[1]
        imgurClient.accountUsername = parseUrl[4].split("=".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()[1]
        imgurClient.accountIdentification = java.lang.Long.valueOf(parseUrl[5].split("=".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()[1])
            .toString()
        if (imgurClient.accessToken != null && imgurClient.refreshToken != null
            && imgurClient.accountUsername != null && imgurClient.accountIdentification != null) {
                imgurClient.isConnected = true
        }
        Toast.makeText(applicationContext, "Welcome " + imgurClient.accountUsername, Toast.LENGTH_SHORT).show()
        finish()
    }

    fun imgurAnthentification() {

        val webView = findViewById<WebView>(R.id.login_webview)
        webView.loadUrl(requestUrl)
        webView.settings.javaScriptEnabled = true

        webView.setWebViewClient(object: WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                parseImgurCallback(url)
                return true
            }
        })

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        imgurAnthentification()
    }
}