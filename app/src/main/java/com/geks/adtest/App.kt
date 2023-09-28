package com.geks.adtest

import android.app.Application
import com.geks.adtest.refactered.AdManager

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        AdManager.init(this)
    }
}