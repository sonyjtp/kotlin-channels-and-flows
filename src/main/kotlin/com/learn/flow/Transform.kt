package com.learn.flow

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.transform

private fun generateInts() = flow {
    var i = 0
    while (true) {
        delay(timeMillis = 100)
        println("Emitting (parent) $i").also { emit(i++) }
    }
}

fun main()  = runBlocking<Unit> {
    launch {
        generateInts().transform {num ->
            if (num  % 2 == 0) {
                println("Emitting (transform-1) ${num * -2}").also { emit(num * -2) }
                println("Emitting (transform-2) ${num * 7}").also { emit(num  * 7) }
            }
            if (num > 10) currentCoroutineContext().cancel()
        }.collect { println("Collected $it") }
    }
}
