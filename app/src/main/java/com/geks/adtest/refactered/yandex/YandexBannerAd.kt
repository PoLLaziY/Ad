package com.geks.adtest.refactered.yandex

import android.view.ViewGroup
import androidx.core.view.get
import androidx.core.view.size
import com.yandex.mobile.ads.banner.BannerAdEventListener
import com.yandex.mobile.ads.banner.BannerAdSize
import com.yandex.mobile.ads.banner.BannerAdView
import com.yandex.mobile.ads.common.AdRequest
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import com.geks.adtest.refactered.utils.Utils
import com.geks.adtest.refactered.utils.interfaces.BannerAd
import com.geks.adtest.refactered.utils.plus
import com.geks.adtest.refactered.utils.useOnlyOnce
import com.geks.adtest.refactered.utils.whenGet
import com.geks.adtest.refactered.utils.with

class YandexBannerAd(private val adId: String): BannerAd {
    private val adRequest: AdRequest get() = AdRequest.Builder().build()
    override fun loadAd(root: ViewGroup, onLoaded: (Boolean) -> Unit) {
        if (!needAddView(root)) {
            onLoaded(true)
            return
        }

        val context = root.context
        val adView = BannerAdView(context)

        val addCallback = Utils::addView.with(root, adView).whenGet(true)
        val resultCallback = useOnlyOnce(addCallback + onLoaded)

        adView.setup(resultCallback)
        adView.loadAd(adRequest)
    }

    private fun needAddView(root: ViewGroup): Boolean {
        for (i in 0 until root.size) {
            if (root[i] is BannerAdView) return false
        }
        return true
    }

    private fun BannerAdView.setup(onLoaded: (loaded: Boolean) -> Unit) {
        val width = Utils.width(context)
        setAdSize(BannerAdSize.stickySize(context, width))
        setAdUnitId(adId)

        setBannerAdEventListener(object : BannerAdEventListener {
            override fun onAdLoaded() {
                onLoaded(true)
            }

            override fun onAdFailedToLoad(p0: AdRequestError) {
                onLoaded(false)
            }

            override fun onAdClicked() {}
            override fun onLeftApplication() {}
            override fun onReturnedToApplication() {}
            override fun onImpression(p0: ImpressionData?) {}
        })
    }
}