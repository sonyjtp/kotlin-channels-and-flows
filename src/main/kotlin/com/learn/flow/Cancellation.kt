package com.learn.flow

import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

private fun generateInts() = flow {
    var i = 0
    while (true) {
        delay(timeMillis = 1000)
        emit(i++).also { print("\nEmitted ${i - 1}") }
    }
}

fun main()  = runBlocking<Unit>{
    launch {
        generateInts().collect {
            print("\t$it collected by A")
            if (it == 5) this.cancel()
        }
    }
    delay (timeMillis = 2300)
    launch {
        generateInts().collect {
            print("\t$it collected by B")
            if (it == 5) this.cancel()
        }
    }
}
