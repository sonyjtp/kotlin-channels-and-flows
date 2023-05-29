package com.learn.channels

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.selects.onTimeout
import kotlinx.coroutines.selects.select


@OptIn(ExperimentalCoroutinesApi::class)
private fun CoroutineScope.produceNumbers() = produce {
    while (true) {
        delay (timeMillis = 1000)
        send(1)
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
fun main() = runBlocking {
    val producer = produceNumbers()
    select {
        producer.onReceive {
            println("Received $it")
        }
        onTimeout(timeMillis = 200) {
            println("Timed out")
        }
    }.also { producer.cancel() }
}
