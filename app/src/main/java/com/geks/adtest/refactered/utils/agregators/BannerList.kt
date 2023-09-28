package com.geks.adtest.refactered.utils.agregators

import android.view.ViewGroup
import androidx.lifecycle.findViewTreeLifecycleOwner
import com.geks.adtest.refactered.utils.inMain
import com.geks.adtest.refactered.utils.interfaces.BannerAd
import com.geks.adtest.refactered.utils.or
import com.geks.adtest.refactered.utils.with

class BannerList(vararg banner: BannerAd) {
    private val banners = banner.asList()

    fun loadAd(root: ViewGroup, setupRoot: (ViewGroup, Boolean) -> Unit) {
        inMain(root.findViewTreeLifecycleOwner()) {
            loadAdWithIndex(root, 0, setupRoot.with(root))
        }
    }

    private fun loadAdWithIndex(
        root: ViewGroup,
        adIndex: Int,
        onResult: (Boolean) -> Unit
    ) {
        val ad = banners.getOrNull(adIndex)

        val callback = if (adIndex == banners.lastIndex || ad == null) {
            onResult
        } else {
            val loadNext = ::loadAdWithIndex.with(root,adIndex + 1, onResult)
            onResult or loadNext
        }

        if (ad == null) {
            callback(false)
        } else {
            ad.loadAd(root, callback)
        }
    }
}