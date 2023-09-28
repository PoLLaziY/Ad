package com.geks.adtest.refactered.utils.interfaces

import android.view.ViewGroup

interface BannerAd {
    fun loadAd(root: ViewGroup, onLoaded: (Boolean) -> Unit = {})
}