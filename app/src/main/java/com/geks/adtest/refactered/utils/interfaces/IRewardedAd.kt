package com.geks.adtest.refactered.utils.interfaces

import android.app.Activity
import com.geks.adtest.refactered.utils.RewardData

interface IRewardedAd {
    fun show(activity: Activity, onShowed: (Boolean) -> Unit, onRewarded: (RewardData) -> Unit)
}