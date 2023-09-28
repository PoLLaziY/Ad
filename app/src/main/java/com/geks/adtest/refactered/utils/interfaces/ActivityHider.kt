package com.geks.adtest.refactered.utils.interfaces

import android.app.Activity

interface ActivityHider {
    fun show(activity: Activity)
    fun hide(activity: Activity)
}