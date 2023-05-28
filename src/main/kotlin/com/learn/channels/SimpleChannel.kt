package com.learn.channels

import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay

fun main()  = runBlocking{
    val channel = Channel<Int>()
    val jobs = mutableListOf<Job>()
    jobs.add( launch {
         for (i in 1..3) {
             delay(timeMillis = 200)
             println("Sending $i")
             channel.send(i)
         }
        channel.close()
    })
    jobs.add( launch {
        for (value in channel) {
            println("Received $value")
        }
     })
    jobs.forEach {it.join()}
    println("Done!")
}
