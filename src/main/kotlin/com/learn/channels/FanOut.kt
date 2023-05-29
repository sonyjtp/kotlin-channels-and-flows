
package com.learn.channels

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlin.random.Random


@OptIn(ExperimentalCoroutinesApi::class)
private suspend fun produceNumbers(scope: CoroutineScope) = scope.produce {
    while(true) {
        delay(timeMillis = 20)
        Random.nextInt(1, until = 100).let { num -> send(num).also { print("\nSending $num ") } }
    }
}

private suspend fun consume(channelId: Int,
    scope: CoroutineScope, producer: ReceiveChannel<Int>) = scope.launch {
        producer.consumeEach { print("\t\tChannel $channelId received $it ") }
}

fun main()  = runBlocking {
    val channel = produceNumbers(this)
    repeat(times = 3) {
        consume(it,this, channel)
    }
    delay(timeMillis = 300)
    channel.cancel()
}


