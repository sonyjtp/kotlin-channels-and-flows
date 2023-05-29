package com.learn.channels

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.selects.select


@OptIn(ExperimentalCoroutinesApi::class)
private suspend fun CoroutineScope.produceNumbers(sideChannel: SendChannel<Int>)  = produce {
        repeat(10) { num ->
            delay(timeMillis = 100)
            select {
                onSend(num) { }
                sideChannel.onSend(num) { }
            }
        }
}

fun main() = runBlocking {
    val sideChannel = Channel<Int>()
    launch {
        sideChannel.consumeEach {
            println("Side received $it")
        }
    }
    val producer = produceNumbers(sideChannel)
    launch {
        producer.consumeEach {
            println("Main received $it")
            delay(timeMillis = 500)
        }
    }

    delay(timeMillis = 1200)
    println("Done").also { sideChannel.cancel() }.also { producer.cancel() }
}
