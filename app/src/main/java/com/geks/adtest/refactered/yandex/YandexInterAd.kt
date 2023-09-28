package com.geks.adtest.refactered.yandex

import android.app.Activity
import android.app.Application
import androidx.lifecycle.LifecycleOwner
import com.yandex.mobile.ads.common.AdError
import com.yandex.mobile.ads.common.AdRequestConfiguration
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.interstitial.InterstitialAd
import com.yandex.mobile.ads.interstitial.InterstitialAdEventListener
import com.yandex.mobile.ads.interstitial.InterstitialAdLoadListener
import com.yandex.mobile.ads.interstitial.InterstitialAdLoader
import com.geks.adtest.refactered.utils.interfaces.Initializable
import com.geks.adtest.refactered.utils.inMain
import com.geks.adtest.refactered.utils.interfaces.FullScreenAd
import com.geks.adtest.refactered.utils.plus
import com.geks.adtest.refactered.utils.useOnlyOnce
import com.geks.adtest.refactered.utils.with

class YandexInterAd(private val adId: String) : FullScreenAd, Initializable {

    private var ad: InterstitialAd? = null
    private var loading = false

    override fun show(activity: Activity, onShowed: (Boolean) -> Unit, onClosed: () -> Unit) {
        val callback = ::init.with(activity.application).inMain() + onShowed
        val onShowedCallback = useOnlyOnce(callback)

        if (ad == null) {
            onShowedCallback(false)
            return
        }

        ad?.setAdEventListener(object :
            InterstitialAdEventListener {
            override fun onAdShown() {
                onShowedCallback(true)
            }

            override fun onAdDismissed() {
                onClosed()
            }

            override fun onAdFailedToShow(p0: AdError) {
                onShowedCallback(false)
            }

            override fun onAdClicked() {}
            override fun onAdImpression(p0: ImpressionData?) {}
        })

        inMain(activity as? LifecycleOwner) {
            ad?.show(activity)
        }
    }

    override fun init(app: Application) {
        if (loading) return
        loading = true

        val interstitialAdLoader = InterstitialAdLoader(app).apply {
            setAdLoadListener(object : InterstitialAdLoadListener {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    loading = false
                    ad = interstitialAd
                }

                override fun onAdFailedToLoad(adRequestError: AdRequestError) {
                    loading = false
                    ad = null
                }
            })
        }

        val adRequestConfiguration = AdRequestConfiguration.Builder(adId).build()

        interstitialAdLoader.loadAd(adRequestConfiguration)
    }
}