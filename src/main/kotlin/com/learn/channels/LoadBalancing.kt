package com.learn.channels

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

private const val TOTAL_WORK_ITEMS = 200
private const val AGENT_COUNT = 10
data class Work(val x: Long, val y: Long, var total: Long = 0L)

private suspend fun agent(input: ReceiveChannel<Work>, output: SendChannel<Work> ) {
    for (work in input) {
        work.total = work.x * work.y
        delay(work.total)
        output.send(work)
    }
}

private suspend fun consumeFromAgent(inputFromAgent: ReceiveChannel<Work>) {
    inputFromAgent.consumeEach { println("Received ${it.total}") }
}

private suspend fun produceToAgent(outputToAgent: SendChannel<Work>) {
    delay(timeMillis = 100)
    repeat(TOTAL_WORK_ITEMS) {
        val work = Work(x = (1L..100).random(), y = (1L..10).random())
        outputToAgent.send(work).also { println("Sending (${work.x}, ${work.y})") }
    }
}

private suspend fun startWork() = coroutineScope{
    val producerChannel = Channel<Work>() //fan-out
    val consumerChannel = Channel<Work>() //fan-in
    repeat(AGENT_COUNT) {
        launch {
            agent(producerChannel, consumerChannel)
        }
    }
    launch { produceToAgent(producerChannel) }
    launch { consumeFromAgent(consumerChannel) }
}

fun main(): Unit = runBlocking {
    startWork()
    delay(timeMillis = 1000)

}
