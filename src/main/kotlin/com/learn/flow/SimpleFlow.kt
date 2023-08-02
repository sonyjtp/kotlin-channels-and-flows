package com.learn.flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

private fun generateInts() = flow {
    repeat(times = 5) {
        delay(timeMillis = 200)
        emit(it)
    }
}

fun main()  = runBlocking<Unit> {
    launch {
        generateInts().collect {
            println("Collected: $it")
        }
    }
}
