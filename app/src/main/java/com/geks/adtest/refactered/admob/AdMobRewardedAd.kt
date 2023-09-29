package com.geks.adtest.refactered.admob

import android.app.Activity
import android.app.Application
import androidx.lifecycle.LifecycleOwner
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.geks.adtest.refactered.utils.interfaces.IRewardedAd
import com.geks.adtest.refactered.utils.interfaces.Initializable
import com.geks.adtest.refactered.utils.RewardData
import com.geks.adtest.refactered.utils.inMain
import com.geks.adtest.refactered.utils.plus
import com.geks.adtest.refactered.utils.useOnlyOnce
import com.geks.adtest.refactered.utils.with

class AdMobRewardedAd(private val adId: String) : IRewardedAd, Initializable {

    private var ad: RewardedAd? = null
    private var loading = false

    override fun show(
        activity: Activity,
        onShowed: (Boolean) -> Unit,
        onRewarded: (RewardData) -> Unit
    ) {
        val callback = ::init.with(activity.application).inMain() + onShowed
        val onShowedCallback = useOnlyOnce(callback)
        val onRewadedCorrect = useOnlyOnce(onRewarded)
        if (ad == null) {
            onShowedCallback(false)
            return
        }

        ad?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent()
                onShowedCallback(true)
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                super.onAdFailedToShowFullScreenContent(p0)
                onShowedCallback(false)
            }
        }

        inMain(activity as? LifecycleOwner) {
            ad?.show(activity) { onRewadedCorrect(RewardData(it.type, it.amount)) }
        }
    }

    override fun init(app: Application) {
        if (loading) return
        loading = true

        val request = AdManagerAdRequest.Builder().build()

        RewardedAd.load(app, adId, request,
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(p0: RewardedAd) {
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


