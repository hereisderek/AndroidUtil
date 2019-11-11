package com.github.hereisderek.androidutil.closeable

import timber.log.Timber
import java.io.Closeable

/**
 *
 * User: derekzhu
 * Date: 12/11/19 9:54 AM
 * Project: android-util
 */


fun Closeable?.closeQuiet(handler: ((Exception)->Unit)? = null) {
    if (this == null) return
    try {
        this.close()
    } catch (e: Exception) {
        Timber.e("Closeable?.closeQuiet encountered exception:${e.message}")
        handler?.invoke(e)
    }
}


/**
 *
 */
fun <T> useOrCreateAndClose(
    generator: ()-> Closeable,
    _closeable: Closeable? = null,
    action: Closeable.(created: Boolean)-> T
) : T {
    val close = _closeable == null // we'll need to create it therefore we'll close it after using
    val closeable = _closeable ?: generator.invoke()
    return action(closeable, !close).also {
        if (close) {
            closeable.closeQuiet()
        }
    }
}