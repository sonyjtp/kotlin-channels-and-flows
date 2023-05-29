package com.learn.channels

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.selects.select


@OptIn(ExperimentalCoroutinesApi::class)
private suspend fun producer1(coroutineScope: CoroutineScope) = coroutineScope.produce {
    delay(timeMillis = 100)
    send("Sending from 1")
}

@OptIn(ExperimentalCoroutinesApi::class)
private suspend fun producer2(coroutineScope: CoroutineScope) = coroutineScope.produce {
    delay(timeMillis = 100)
    send("Sending from 2")
}

fun main() = runBlocking {
    val p1 = producer1(this)
    val p2 = producer2(this)
    repeat(times = 15) {// keep receiving 15 times
        val result = select {
            p1.onReceiveCatching {
                it.getOrNull() ?: "Channel 1 closed"
            }
            p2.onReceiveCatching {
                it.getOrNull() ?: "Channel 2 closed"
            }
        }
        println(result)
    }

    delay(timeMillis = 1000)
    println("Cancelling").also { p1.cancel()}.also { p2.cancel() }
}
