package com.rvigo.springbatchdemo.step.personProcess.writer

import com.rvigo.springbatchdemo.CustomRetryException
import com.rvigo.springbatchdemo.entity.ProcessedPerson
import com.rvigo.springbatchdemo.logger
import com.rvigo.springbatchdemo.repository.ProcessedPersonRepository
import org.springframework.batch.item.Chunk
import org.springframework.batch.item.ItemWriter

class PersonWriter(private val repository: ProcessedPersonRepository) : ItemWriter<ProcessedPerson> {
    private val log = logger()

    override fun write(chunk: Chunk<out ProcessedPerson>) {
//        if (7 in chunk.map { it.id }) {
//            log.warn("found 7")
//            throw CustomRetryException()
//        }
        repository.saveAll(chunk)
    }
}