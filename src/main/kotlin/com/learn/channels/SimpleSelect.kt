package com.learn.channels

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.selects.select


@OptIn(ExperimentalCoroutinesApi::class)
private suspend fun producer1(coroutineScope: CoroutineScope) = coroutineScope.produce {
    var count = 100
    while (true) {
        delay(timeMillis = 100)
        send("From 1: ${count++}").also { println("Sent ${count - 1}") }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
private suspend fun producer2(coroutineScope: CoroutineScope) = coroutineScope.produce {
    var count = 100
    while (true) {
        delay(timeMillis = 100)
        send("From 2: ${count++}").also { println("Sent ${count - 1}") }
    }
}

fun main() = runBlocking {
    val p1 = producer1(this)
    val p2 = producer2(this)
    repeat(times = 15) {// keep receiving 15 times
        select {
            p1.onReceive { println("Received $it") }
            p2.onReceive { println("Received $it") }
        }
    }
    delay(timeMillis = 1000)
    println("Cancelling").also { p1.cancel()}.also { p2.cancel() }
}
