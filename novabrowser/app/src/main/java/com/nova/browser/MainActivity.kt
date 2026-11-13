package com.nova.browser

import android.app.Activity
import android.os.Bundle
import android.view.ViewGroup
import android.view.Gravity
import android.graphics.Color
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout

class MainActivity : Activity() {
    private lateinit var webView: WebView
    private lateinit var addressBar: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.WHITE)
        }

        val toolbar = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(8, 8, 8, 8)
            setBackgroundColor(Color.rgb(25, 25, 35))
        }

        addressBar = EditText(this).apply {
            hint = "اكتب عنوان الموقع أو ابحث"
            singleLine = true
            setTextColor(Color.WHITE)
            setHintTextColor(Color.LTGRAY)
            setSelectAllOnFocus(true)
        }

        val goButton = Button(this).apply {
            text = "اذهب"
            setOnClickListener { openAddress() }
        }

        toolbar.addView(
            addressBar,
            LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
        )
        toolbar.addView(goButton)

        webView = WebView(this).apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            webViewClient = WebViewClient()
        }

        root.addView(toolbar)
        root.addView(
            webView,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0,
                1f
            )
        )

        setContentView(root)
        webView.loadUrl("https://www.google.com")
    }

    private fun openAddress() {
        var address = addressBar.text.toString().trim()
        if (address.isEmpty()) return

        address = if (
            address.startsWith("https://") ||
            address.startsWith("http://")
        ) {
            address
        } else if (address.contains(".") && !address.contains(" ")) {
            "https://$address"
        } else {
            "https://www.google.com/search?q=" +
                java.net.URLEncoder.encode(address, "UTF-8")
        }

        webView.loadUrl(address)
    }

    @Deprecated("Deprecated in Android")
    override fun onBackPressed() {
        if (::webView.isInitialized && webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        if (::webView.isInitialized) webView.destroy()
        super.onDestroy()
    }
}
