package com.geks.adtest.refactered.utils

import android.app.Application
import android.os.Build
import android.webkit.WebView
import com.geks.adtest.refactered.utils.interfaces.Initializable

class WebViewConfigurator: Initializable {
    override fun init(app: Application) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val process = Application.getProcessName()
            if (app.packageName != process) {
                WebView.setDataDirectorySuffix(process)
            }
        }
    }
}