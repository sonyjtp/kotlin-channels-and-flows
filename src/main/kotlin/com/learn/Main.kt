package com.learn

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(args: Array<String>) = runBlocking {
    val parentJob = launch {
        executeSimpleChannel()
    }
    parentJob.join()
    println("Done!")
}

private suspend fun executeSimpleChannel()  = coroutineScope {
    val channel = Channel<Int>()
    val producer = launch {
        delay(timeMillis = 100)
        channel.send(1)
    }
    val consumer = launch {
        println(channel.receive())
    }
    producer.join()
    consumer.join()
}

