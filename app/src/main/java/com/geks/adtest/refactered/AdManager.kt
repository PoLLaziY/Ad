package com.geks.adtest.refactered

import android.app.Activity
import android.app.Application
import android.view.ViewGroup
import com.geks.adtest.refactered.admob.AdMobInterAd
import com.geks.adtest.refactered.admob.AdMobOpenAd
import com.geks.adtest.refactered.admob.AdMobBannerAd
import com.geks.adtest.refactered.utils.agregators.BannerList
import com.geks.adtest.refactered.utils.BannerRootSetuper
import com.geks.adtest.refactered.utils.FrameActivityHider
import com.geks.adtest.refactered.utils.agregators.FullScreenAdList
import com.geks.adtest.refactered.utils.interfaces.Initializable
import com.geks.adtest.refactered.utils.Lockable
import com.geks.adtest.refactered.utils.OnAppOpenCaller
import com.geks.adtest.refactered.utils.WebViewConfigurator
import com.geks.adtest.refactered.utils.or
import com.geks.adtest.refactered.utils.plus
import com.geks.adtest.refactered.utils.with
import com.geks.adtest.refactered.yandex.YandexInterAd
import com.geks.adtest.refactered.yandex.YandexBannerAd

object AdManager : Lockable(), Initializable {

    private val bannerAds: BannerList by lazy {
        BannerList(
            AdMobBannerAd("ca-app-pub-3940256099942544/6300978111"),
            YandexBannerAd("R-M-DEMO-320x50")
        )
    }
    private val interAds: FullScreenAdList by lazy {
        FullScreenAdList(
            AdMobInterAd("ca-app-pub-3940256099942544/1033173712"),
            YandexInterAd("R-M-DEMO-interstitial")
        )
    }
    private val openAds: FullScreenAdList by lazy {
        FullScreenAdList(AdMobOpenAd("ca-app-pub-3940256099942544/3419835294"))
    }
    private val hider by lazy { FrameActivityHider() }
    private val bannerRootSetuper by lazy { BannerRootSetuper() }
    private var click = 1
        set(value) {
            field = value
            if (field >= 4) field = 1
            if (field < 1) field = 1
        }

    override fun init(app: Application) {
        WebViewConfigurator().init(app)
        OnAppOpenCaller(::showOpenAd).init(app)
        interAds.init(app)
        openAds.init(app)
    }

    fun loadAd(root: ViewGroup?) {
        if (root == null) return
        bannerRootSetuper.preloadSetup(root)
        bannerAds.loadAd(root, bannerRootSetuper::setup)
    }

    fun showOpenAd(activity: Activity, onClose: () -> Unit = {}) {
        if (locked) return
        lock(openAds)
        val showHider = hider::show.with(activity)
        val hideHider = hider::hide.with(activity)
        val afterAd = ::unlock.with(openAds) + onClose
        val onShowedInter = showHider or afterAd
        val onCloseAd = hideHider + afterAd
        val showInter = interAds::showAd.with(activity, onShowedInter, onCloseAd)
        val onShowedOpen = showHider or showInter
        openAds.showAd(activity, onShowedOpen, onCloseAd)
    }

    fun showInterAd(activity: Activity, onClose: () -> Unit) {
        if (locked) {
            onClose()
            return
        }
        lock(interAds)
        val unlock = ::unlock.with(interAds)
        val onShow = hider::show.with(activity) or (unlock + onClose)
        val onCloseCorrected = hider::hide.with(activity) + unlock + onClose
        interAds.showAd(activity, onShow, onCloseCorrected)
    }

    fun navigate(activity: Activity, onClose: () -> Unit) {
        click++
        if (click % 3 == 0) {
            showInterAd(activity, onClose)
        } else {
            onClose()
        }
    }
}

