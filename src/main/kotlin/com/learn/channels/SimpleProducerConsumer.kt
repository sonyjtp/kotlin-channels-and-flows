@file:OptIn(ExperimentalCoroutinesApi::class)

package com.learn.channels

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce

@OptIn(ExperimentalCoroutinesApi::class)
private fun CoroutineScope.produceSquares(): ReceiveChannel<Int> = produce {
    delay(timeMillis = 100)
    for (i in 1..3) {
        print("\nSending ${i * i}")
        send(i * i)
    }
}

fun main()  = runBlocking<Unit> {
    val channel = produceSquares()
    launch {
        channel.consumeEach {
            print("\nReceived $it")
        }
    }
}
