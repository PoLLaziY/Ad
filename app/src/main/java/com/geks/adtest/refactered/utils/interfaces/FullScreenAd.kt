package com.geks.adtest.refactered.utils.interfaces

import android.app.Activity

interface FullScreenAd {
    fun show(activity: Activity, onShowed: (Boolean) -> Unit, onClosed: () -> Unit)
}