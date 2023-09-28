package com.geks.adtest.refactered.utils
abstract class Lockable {

    private val keys: MutableSet<Any> = mutableSetOf()

    val locked: Boolean get() = keys.isNotEmpty()
    val unlocked: Boolean get() = keys.isEmpty()

    fun lock(on: Any) {
        keys.add(on)
    }
    fun unlock(on: Any) {
        keys.remove(on)
    }
}
