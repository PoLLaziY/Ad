package com.geks.adtest.refactered.utils

import android.content.Context
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

object Utils {
    fun width(context: Context): Int {
        val resources = context.resources
        return (resources.displayMetrics.widthPixels / resources.displayMetrics.density).toInt()
    }

    fun addView(root: ViewGroup, view: View) {
        root.addView(view)
    }
}

fun <T> useOnlyOnce(action: (T) -> Unit): (T) -> Unit {
    val invoked = AtomicBoolean(false)
    return {
        if (invoked.compareAndSet(false, true)) {
            action.invoke(it)
        } else {
            throw Exception("Only once use permitted $action")
        }
    }
}

fun useOnlyOnce(action: () -> Unit): () -> Unit {
    val invoked = AtomicBoolean(false)
    return {
        if (invoked.compareAndSet(false, true)) {
            action.invoke()
        } else {
            throw Exception("Only once use permitted")
        }
    }
}

fun <T, I, O> ((T, I, O) -> Unit).with(arg: T): (I, O) -> Unit {
    return { i, o ->
        this.invoke(arg, i, o)
    }
}

fun <T, I, O> ((T, I, O) -> Unit).with(arg1: T, arg2: I): (O) -> Unit {
    return { o ->
        this.invoke(arg1, arg2, o)
    }
}

fun <T, I, O> ((T, I, O) -> Unit).with(arg1: T, arg2: I, arg3: O): () -> Unit {
    return {
        this.invoke(arg1, arg2, arg3)
    }
}

fun <T, I> ((T, I) -> Unit).with(arg: T): (I) -> Unit {
    return { i ->
        this.invoke(arg, i)
    }
}

fun <T, I> ((T, I) -> Unit).with(arg1: T, arg2: I): () -> Unit {
    return {
        this.invoke(arg1, arg2)
    }
}

fun <T> ((T) -> Unit).with(arg: T): () -> Unit {
    return {
        this.invoke(arg)
    }
}

fun <T> (() -> Unit).whenGet(arg: T): (T) -> Unit {
    return {
        if (it == arg) this()
    }
}

fun <T, I> ((I) -> Unit).withWhenGet(with: I, whenGet: T): (T) -> Unit {
    return this.with(with).whenGet(whenGet)
}

fun <T> ((T) -> Unit).whenGet(arg: T): (T) -> Unit {
    return {
        if (it == arg) this(it)
    }
}

fun <T> (() -> Unit).anyway(): (T) -> Unit {
    return {
        this()
    }
}

fun inMain(lifecycleOwner: LifecycleOwner? = null, action: () -> Unit) {
    if (Looper.myLooper() == Looper.getMainLooper()) {
        action()
    } else if (lifecycleOwner != null) {
        lifecycleOwner.lifecycleScope.launch(Dispatchers.Main) { action() }
    } else {
        CoroutineScope(Dispatchers.Main).launch { action() }
    }
}

fun <T> ((T) -> Unit).inMain(lifecycleOwner: LifecycleOwner? = null): (T) -> Unit {
    return { inMain(lifecycleOwner) { this(it) } }
}

fun (() -> Unit).inMain(lifecycleOwner: LifecycleOwner? = null): () -> Unit {
    return { inMain(lifecycleOwner) { this() } }
}

operator fun <T> ((T) -> Unit).plus(other: (T) -> Unit): (T) -> Unit {
    return {
        this.invoke(it)
        other.invoke(it)
    }
}

operator fun <T> (() -> Unit).plus(other: (T) -> Unit): (T) -> Unit {
    return {
        this.invoke()
        other.invoke(it)
    }
}

operator fun <T> ((T) -> Unit).plus(other: () -> Unit): (T) -> Unit {
    return {
        this.invoke(it)
        other.invoke()
    }
}

operator fun (() -> Unit).plus(other: () -> Unit): () -> Unit {
    return {
        this.invoke()
        other.invoke()
    }
}

infix fun (() -> Unit).or(other: () -> Unit): (Boolean) -> Unit {
    return {
        if (it) {
            this()
        } else {
            other()
        }
    }
}

infix fun ((Boolean) -> Unit).or(other: () -> Unit): (Boolean) -> Unit {
    return {
        if (it) {
            this(it)
        } else {
            other()
        }
    }
}

infix fun (() -> Unit).or(other: (Boolean) -> Unit): (Boolean) -> Unit {
    return {
        if (it) {
            this()
        } else {
            other(false)
        }
    }
}

infix fun ((Boolean) -> Unit).or(other: (Boolean) -> Unit): (Boolean) -> Unit {
    return {
        if (it) {
            this(true)
        } else {
            other(false)
        }
    }
}