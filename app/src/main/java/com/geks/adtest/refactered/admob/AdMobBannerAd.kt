package com.geks.adtest.refactered.admob

import android.view.ViewGroup
import androidx.core.view.get
import androidx.core.view.size
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.geks.adtest.refactered.utils.Utils
import com.geks.adtest.refactered.utils.interfaces.BannerAd
import com.geks.adtest.refactered.utils.plus
import com.geks.adtest.refactered.utils.useOnlyOnce
import com.geks.adtest.refactered.utils.whenGet
import com.geks.adtest.refactered.utils.with

class AdMobBannerAd(private val adId: String) : BannerAd {

    private val adRequest: AdRequest get() = AdRequest.Builder().build()
    override fun loadAd(root: ViewGroup, onLoaded: (Boolean) -> Unit) {
        if (!needAddView(root)) {
            onLoaded(true)
            return
        }

        val adView = AdView(root.context)
        val addViewCallback = Utils::addView.with(root, adView).whenGet(true)
        val onLoadedFixed = useOnlyOnce(addViewCallback + onLoaded)

        adView.setup(onLoadedFixed)
        adView.loadAd(adRequest)
    }

    private fun needAddView(root: ViewGroup): Boolean {
        for (i in 0 until root.size) {
            if (root[i] is AdView) return false
        }
        return true
    }

    private fun AdView.setup(onLoad: (Boolean) -> Unit) {
        val context = this.context
        val width = Utils.width(context)
        val height = 55
        val adSize = AdSize.getInlineAdaptiveBannerAdSize(width, height)
        this.adUnitId = adId
        this.setAdSize(adSize)

        this.adListener = object : AdListener() {
            override fun onAdLoaded() {
                super.onAdLoaded()
                onLoad(true)
            }

            override fun onAdFailedToLoad(p0: LoadAdError) {
                super.onAdFailedToLoad(p0)
                onLoad(false)
            }
        }
    }
}