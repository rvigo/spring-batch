package com.rvigo.springbatchdemo

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringBatchDemoApplication

fun main(args: Array<String>) {
    runApplication<SpringBatchDemoApplication>(*args)
}

inline fun <reified T> T.logger(): Logger = LoggerFactory.getLogger(T::class.java)
