
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
private suspend fun produceBookId(scope: CoroutineScope) = scope.produce {
    var i = 1
    while(true) {
        delay(timeMillis = 20)
        send(i++).also { println("Sending ${i - 1}") }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
private suspend fun <T, R> ReceiveChannel<T>.map(scope: CoroutineScope, transform: (T) -> R): ReceiveChannel<R> {
    val receiveChannel = this
    return scope.produce { receiveChannel.consumeEach { send(transform(it)) } }
}

private suspend fun consume(
    scope: CoroutineScope, producer: ReceiveChannel<Int>) = scope.launch {
        producer.consumeEach { println("Received $it") }
}

fun main()  = runBlocking {
    val producer = produceBookId(this).map(this) { it * it }
    consume(this, producer)
    delay(timeMillis = 200)
    producer.cancel()
}



