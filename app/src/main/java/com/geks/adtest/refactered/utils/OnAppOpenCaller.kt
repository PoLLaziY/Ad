package com.geks.adtest.refactered.utils

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.geks.adtest.refactered.utils.interfaces.Initializable

class OnAppOpenCaller(private val callback: (Activity) -> Unit) : Initializable,
    Application.ActivityLifecycleCallbacks {

    private var lastActivityName: String? = null
    private var lastConfiguration: Int? = null
    private var lastPaused: Long = 0
    private val closeAppInterval = 1_000

    override fun init(app: Application) {
        app.registerActivityLifecycleCallbacks(this)
    }

    override fun onActivityStarted(activity: Activity) {
        val name = activity.javaClass.name
        val configuration = activity.resources.configuration.hashCode()
        val fromPauseTime = System.currentTimeMillis() - lastPaused
        if (lastActivityName != name || fromPauseTime < closeAppInterval) {
            lastActivityName = name
            lastConfiguration = configuration
            lastPaused = 0
            return
        }
        callback(activity)
    }

    override fun onActivityCreated(p0: Activity, p1: Bundle?) {}
    override fun onActivityResumed(p0: Activity) {}

    override fun onActivityPaused(p0: Activity) {
        if (lastActivityName == p0.javaClass.name) {
            lastPaused = System.currentTimeMillis()
        }
    }

    override fun onActivityStopped(p0: Activity) {}
    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {}
    override fun onActivityDestroyed(p0: Activity) {
        if (lastActivityName == p0.javaClass.name) {
            lastPaused = System.currentTimeMillis()
        }
    }
}