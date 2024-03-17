package com.rvigo.springbatchdemo.step.send.writer

import com.rvigo.springbatchdemo.CustomRetryException
import com.rvigo.springbatchdemo.entity.ProcessedPerson
import com.rvigo.springbatchdemo.infra.Sender
import com.rvigo.springbatchdemo.logger
import org.springframework.batch.item.Chunk
import org.springframework.batch.item.ItemWriter

class SendWriter(private val sender: Sender<ProcessedPerson>) : ItemWriter<ProcessedPerson> {
    private val log = logger()

    override fun write(chunk: Chunk<out ProcessedPerson>) {
        if (7 in chunk.map { it.id }) {
            log.warn("found 7")
            throw CustomRetryException()
        }

        chunk.forEach(sender::send)
    }
}