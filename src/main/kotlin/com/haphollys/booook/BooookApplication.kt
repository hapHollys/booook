package com.haphollys.booook

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BooookApplication

fun main(args: Array<String>) {
    runApplication<BooookApplication>(*args)
}
