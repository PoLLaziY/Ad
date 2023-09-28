package com.geks.adtest.refactered.utils.agregators

import android.app.Activity
import android.app.Application
import com.geks.adtest.refactered.utils.interfaces.FullScreenAd
import com.geks.adtest.refactered.utils.interfaces.Initializable
import com.geks.adtest.refactered.utils.or
import com.geks.adtest.refactered.utils.with

class FullScreenAdList(vararg ads: FullScreenAd) : Initializable {
    private val ads = ads.asList()

    fun showAd(activity: Activity, onShowed: (Boolean) -> Unit, onClosed: () -> Unit) {
        showAdWithIndex(activity, 0, FullScreenAdCallback(onShowed, onClosed))
    }

    private fun showAdWithIndex(
        activity: Activity,
        index: Int,
        callback: FullScreenAdCallback
    ) {
        val ad = ads.getOrNull(index)

        val onShowed = if (ad == null || index == ads.lastIndex) {
            callback.onShowed
        } else {
            val tryNext = ::showAdWithIndex.with(activity, index + 1, callback)
            callback.onShowed or tryNext
        }

        if (ad == null) {
            callback.onShowed(false)
        } else {
            ad.show(activity, onShowed, callback.onClosed)
        }
    }

    override fun init(app: Application) {
        ads.forEach { if (it is Initializable) it.init(app) }
    }
}

data class FullScreenAdCallback(
    val onShowed: (Boolean) -> Unit,
    val onClosed: () -> Unit
)