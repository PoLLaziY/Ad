package com.geks.adtest.refactered.utils

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.LifecycleOwner
import com.geks.adtest.R
import com.geks.adtest.refactered.utils.interfaces.ActivityHider
import java.lang.ref.WeakReference

class FrameActivityHider : ActivityHider, Application.ActivityLifecycleCallbacks {

    private val frameId = FrameActivityHider::class.java.name.hashCode()
    private var lastActivity: String? = null
    private var weakActivity: WeakReference<Activity?> = WeakReference(null)

    override fun show(activity: Activity) {
        loadFrameInMain(activity)
        activity.application.unregisterActivityLifecycleCallbacks(this)
        activity.application.registerActivityLifecycleCallbacks(this)
        lastActivity = activity::class.java.name
    }

    override fun hide(activity: Activity) {
        val actualActivity = weakActivity.get() ?: activity
        actualActivity.application.unregisterActivityLifecycleCallbacks(this)

        val frame = actualActivity.findViewById<FrameLayout>(frameId) ?: return

        val root = frame.parent as? ViewGroup ?: return

        inMain(actualActivity as? LifecycleOwner) {
            root.removeView(frame)
        }
    }

    private fun loadFrameInMain(activity: Activity) {
        inMain(activity as? LifecycleOwner) {
            loadFrame(WeakReference(activity))
        }
    }

    private fun loadFrame(weakActivity: WeakReference<Activity>) {
        this.weakActivity = WeakReference(weakActivity.get())
        val activity = weakActivity.get() ?: return
        val frame = FrameLayout(activity)
        frame.id = frameId
        val params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        activity.addContentView(frame, params)
        frame.setBackgroundColor(activity.getColor(R.color.black))
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        if (activity::class.java.name == lastActivity) {
            loadFrameInMain(activity)
        }
    }

    override fun onActivityStarted(activity: Activity) {

    }

    override fun onActivityResumed(activity: Activity) {

    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {

    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityDestroyed(activity: Activity) {

    }
}