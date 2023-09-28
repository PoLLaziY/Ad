package com.geks.adtest.refactered.utils

import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.view.size
import androidx.lifecycle.findViewTreeLifecycleOwner

class BannerRootSetuper {

    fun preloadSetup(root: ViewGroup) {
        inMain(root.findViewTreeLifecycleOwner()) {
            root.isVisible = false
        }
    }

    fun setup(root: ViewGroup, loaded: Boolean) {
        inMain(root.findViewTreeLifecycleOwner()) {
            root.isVisible = loaded
            if (root.size < 1) return@inMain
            if (loaded) {
                for (i in 0 until root.size - 1) {
                    root.removeViewAt(i)
                }
                if (root.layoutParams.height == ViewGroup.LayoutParams.WRAP_CONTENT) return@inMain
                root.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                root.layoutParams = root.layoutParams
            }
        }
    }
}