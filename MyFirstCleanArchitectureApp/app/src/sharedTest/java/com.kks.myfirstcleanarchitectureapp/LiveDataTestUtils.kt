package com.kks.myfirstcleanarchitectureapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

//fun <T> LiveData<T>.getOrAwaitValue(): T {
//
//    var data: T? = null
//    val latch = CountDownLatch(1)
//    val observer = object : Observer<T> {
//        override fun onChanged(t: T) {
//            data = t
//            latch.countDown()
//            this@getOrAwaitValue.removeObserver(this)
//        }
//    }
//    this.observeForever(observer)
//    try {
//        if (!latch.await(2,TimeUnit.SECONDS)) { /* Only wait for 2 seconds for livedata to set value for first time*/
//            throw TimeoutException("Live Data Never gets its value")
//        }
//    } finally {
//        this.removeObserver(observer)
//    }
//    return data as T
//}

fun <T> LiveData<T>.getOrAwaitValue(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS,
    afterObserve: () -> Unit = {}
): T {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(o: T?) {
            data = o
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }
    this.observeForever(observer)

    afterObserve.invoke()

    // Don't wait indefinitely if the LiveData is not set.
    if (!latch.await(time, timeUnit)) {
        this.removeObserver(observer)
        throw TimeoutException("LiveData value was never set.")
    }

    @Suppress("UNCHECKED_CAST")
    return data as T
}

/**
 * Observes a [LiveData] until the `block` is done executing.
 */
fun <T> LiveData<T>.observeForTesting(block: () -> Unit) {
    val observer = Observer<T> {}
    try {
        observeForever(observer)
        block()
    } finally {
        removeObserver(observer)
    }
}