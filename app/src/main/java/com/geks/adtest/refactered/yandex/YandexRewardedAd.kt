package com.geks.adtest.refactered.yandex

import android.app.Activity
import android.app.Application
import androidx.lifecycle.LifecycleOwner
import com.yandex.mobile.ads.common.AdRequestConfiguration
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.rewarded.Reward
import com.yandex.mobile.ads.rewarded.RewardedAd
import com.yandex.mobile.ads.rewarded.RewardedAdEventListener
import com.yandex.mobile.ads.rewarded.RewardedAdLoadListener
import com.yandex.mobile.ads.rewarded.RewardedAdLoader
import com.geks.adtest.refactered.utils.interfaces.Initializable
import com.geks.adtest.refactered.utils.RewardData
import com.geks.adtest.refactered.utils.inMain
import com.geks.adtest.refactered.utils.interfaces.IRewardedAd
import com.geks.adtest.refactered.utils.plus
import com.geks.adtest.refactered.utils.useOnlyOnce
import com.geks.adtest.refactered.utils.with

class YandexRewardedAd(private val adId: String) : IRewardedAd, Initializable {

    private var reserveRewardedAd: RewardedAd? = null
    private var loadingReserve = false

    override fun init(app: Application) {
        if (loadingReserve) return
        loadingReserve = true

        val rewardedAdLoader = RewardedAdLoader(app).apply {
            setAdLoadListener(object : RewardedAdLoadListener {
                override fun onAdLoaded(rewardedAd: RewardedAd) {
                    loadingReserve = false
                    reserveRewardedAd = rewardedAd
                }

                override fun onAdFailedToLoad(adRequestError: AdRequestError) {
                    loadingReserve = false
                    reserveRewardedAd = null
                }
            })
        }

        val adRequestConfiguration = AdRequestConfiguration.Builder(adId).build()
        rewardedAdLoader.loadAd(adRequestConfiguration)
    }

    override fun show(
        activity: Activity,
        onShowed: (Boolean) -> Unit,
        onRewarded: (RewardData) -> Unit
    ) {
        val callback = ::init.with(activity.application).inMain() + onShowed
        val onShowedCallback = useOnlyOnce(callback)
        val onRewardedCorrect = useOnlyOnce(onRewarded)
        if (reserveRewardedAd == null) {
            onShowedCallback(false)
            return
        }
        reserveRewardedAd?.setAdEventListener(object :
            RewardedAdEventListener {
            override fun onAdShown() {
            }

            override fun onAdDismissed() {
                onShowedCallback(true)
            }

            override fun onAdFailedToShow(p0: com.yandex.mobile.ads.common.AdError) {
                onShowedCallback(false)
            }

            override fun onRewarded(p0: Reward) {
                onRewardedCorrect(RewardData(p0.type, p0.amount))
            }

            override fun onAdClicked() {}
            override fun onAdImpression(p0: ImpressionData?) {}
        })

        inMain(activity as? LifecycleOwner) {
            reserveRewardedAd?.show(activity)
        }
    }
}