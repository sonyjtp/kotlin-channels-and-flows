package com.learn

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking

fun generateInts() = flow {
    repeat(times = 5) {
        delay(timeMillis = 200)
        emit(it)
    }
}

fun main()  = runBlocking {
    generateInts().collect {
        println("Collected: $it")
    }
}
