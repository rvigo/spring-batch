package com.rvigo.springbatchdemo.infra

import com.rvigo.springbatchdemo.logger

class Sender<T> {
    private val log = logger()

    fun send(item: T) {
        log.info("sending item $item to queue")
    }
}