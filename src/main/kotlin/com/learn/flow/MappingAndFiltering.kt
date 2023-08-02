package com.learn.flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

private suspend fun generateInts() = flow {
    var value = 0
    while(true) {
        delay(timeMillis = 100)
        println("Sending $value").also { emit(value++) }
    }
}

fun main()  = runBlocking<Unit>{
    launch {
        generateInts().filter { it % 2 == 0 }
            .map { it * it }
            .collect { println("Collected $it") }
    }
}
