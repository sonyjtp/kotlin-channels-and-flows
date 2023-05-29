package com.learn.channels

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.consumeEach


private suspend fun produceNumbers(scope: CoroutineScope, channel: SendChannel<Int>, delay: Long) = scope.launch {
    var x = delay.toInt()
    while (true) {
        channel.send(x++).also { println("Sending $x") }
        delay(delay)
    }
}

private suspend fun consume(scope: CoroutineScope, channel: ReceiveChannel<Int>) = scope.launch {
    channel.consumeEach {
        println("Consumed $it")
    }
}

fun main()  = runBlocking{
    val channel = Channel<Int>()
    produceNumbers(this, channel, delay = 500)
    produceNumbers(this, channel, delay = 200)
    consume(this, channel)
    delay(timeMillis = 1000)
    channel.cancel()
}
