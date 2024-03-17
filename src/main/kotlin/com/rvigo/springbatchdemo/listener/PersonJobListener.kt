package com.rvigo.springbatchdemo.listener

import com.rvigo.springbatchdemo.repository.PersonRepository
import com.rvigo.springbatchdemo.repository.ProcessedPersonRepository
import com.rvigo.springbatchdemo.logger
import org.springframework.batch.core.ExitStatus
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobExecutionListener

class PersonJobListener(
        private val personRepository: PersonRepository,
        private val processedPersonRepository: ProcessedPersonRepository
) : JobExecutionListener {

    private val log = logger()

    override fun beforeJob(jobExecution: JobExecution) {
        log.info("starting job")
        val count = personRepository.count()
        log.info("about to process $count items")
    }

    override fun afterJob(jobExecution: JobExecution) {
        log.info("job finished")
        if (jobExecution.exitStatus.exitCode == ExitStatus.FAILED.exitCode) {
            log.error("failure! ${jobExecution.allFailureExceptions}")
        } else {
            val processed = processedPersonRepository.count()
            log.info("processed $processed items")
        }
    }
}