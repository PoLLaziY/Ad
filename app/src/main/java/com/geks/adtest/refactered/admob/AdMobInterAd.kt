package com.geks.adtest.refactered.admob

import android.app.Activity
import android.app.Application
import androidx.lifecycle.LifecycleOwner
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.geks.adtest.refactered.utils.interfaces.Initializable
import com.geks.adtest.refactered.utils.inMain
import com.geks.adtest.refactered.utils.interfaces.FullScreenAd
import com.geks.adtest.refactered.utils.plus
import com.geks.adtest.refactered.utils.useOnlyOnce
import com.geks.adtest.refactered.utils.with

class AdMobInterAd(private val adId: String) : FullScreenAd, Initializable {
    private var loading = false
    private var ad: InterstitialAd? = null

    override fun show(activity: Activity, onShowed: (Boolean) -> Unit, onClosed: () -> Unit) {
        val callback = ::init.with(activity.application).inMain() + onShowed
        val correctedCallback = useOnlyOnce(callback)

        if (ad == null) {
            correctedCallback(false)
            return
        }

        ad?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdShowedFullScreenContent() {
                super.onAdShowedFullScreenContent()
                correctedCallback(true)
            }

            override fun onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent()
                onClosed()
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                super.onAdFailedToShowFullScreenContent(p0)
                correctedCallback(false)
            }
        }

        inMain(activity as? LifecycleOwner) {
            ad?.show(activity)
        }
    }

    override fun init(app: Application) {
        if (loading) return

        loading = true

        val request = AdRequest.Builder().build()
        InterstitialAd.load(app, adId, request,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(p0: InterstitialAd) {
                    super.onAdLoaded(p0)
                    loading = false
                    ad = p0
                }

                override fun onAdFailedToLoad(p0: LoadAdError) {
                    super.onAdFailedToLoad(p0)
                    loading = false
                    ad = null
                }
            })
    }
}