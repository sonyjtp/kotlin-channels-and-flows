package com.learn.channels

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel


private suspend fun produceNumbers(scope: CoroutineScope, channel: SendChannel<Int>) = scope.launch {
    repeat(times = 5) {
        num ->
        channel.send(num).also { println("Sending $num") } }
}

private suspend fun consume(scope: CoroutineScope, channel: ReceiveChannel<Int>) = scope.launch {
    println("Consumer starting")
    delay(timeMillis = 1000) // for simulating consumer taking some time to start
    println("Consumer started")
    for (value in channel) {
        println("Received $value")
    }
}

fun main()  = runBlocking {
    val channel = Channel<Int>(capacity = 5)
    val producer = produceNumbers(this, channel)
    val consumer = consume(this, channel)
    delay(timeMillis = 500)
    println("Cancelling producer")
    producer.cancel()
    println("Producer cancelled")
    delay(timeMillis = 2000)
    consumer.cancelAndJoin()
    println("Consumer cancelled")
}
