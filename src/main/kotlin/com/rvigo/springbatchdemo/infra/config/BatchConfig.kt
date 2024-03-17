package com.rvigo.springbatchdemo.infra.config

import com.rvigo.springbatchdemo.CustomRetryException
import com.rvigo.springbatchdemo.entity.Person
import com.rvigo.springbatchdemo.entity.ProcessedPerson
import com.rvigo.springbatchdemo.infra.Sender
import com.rvigo.springbatchdemo.listener.PersonJobListener
import com.rvigo.springbatchdemo.repository.PersonRepository
import com.rvigo.springbatchdemo.repository.ProcessedPersonRepository
import com.rvigo.springbatchdemo.step.personProcess.processor.PersonProcessor
import com.rvigo.springbatchdemo.step.personProcess.writer.PersonWriter
import com.rvigo.springbatchdemo.step.send.writer.SendWriter
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.data.RepositoryItemReader
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.Sort
import org.springframework.transaction.PlatformTransactionManager

@Configuration
class BatchConfig {
    @Bean
    fun writer(processedPersonRepository: ProcessedPersonRepository): PersonWriter = PersonWriter(processedPersonRepository)

    @Bean
    fun processor(): PersonProcessor = PersonProcessor()

    @Bean
    fun listener(
            personRepository: PersonRepository,
            processedPersonRepository: ProcessedPersonRepository
    ): PersonJobListener = PersonJobListener(
            personRepository,
            processedPersonRepository
    )

    @Bean("process")
    fun processReader(personRepository: PersonRepository): RepositoryItemReader<Person> =
            RepositoryItemReaderBuilder<Person>()
                    .repository(personRepository)
                    .name("jpaRepository")
                    .methodName("findAll")
                    .pageSize(10)
                    .sorts(mapOf("id" to Sort.Direction.ASC))
                    .build()

    @Bean("send")
    fun sendReader(processedPersonRepository: ProcessedPersonRepository): RepositoryItemReader<ProcessedPerson> =
            RepositoryItemReaderBuilder<ProcessedPerson>()
                    .repository(processedPersonRepository)
                    .name("jpaRepository")
                    .methodName("findAll")
                    .pageSize(10)
                    .sorts(mapOf("id" to Sort.Direction.ASC))
                    .build()

    @Bean
    fun sender() = Sender<ProcessedPerson>()

    @Bean
    fun sendWriter(sender: Sender<ProcessedPerson>) = SendWriter(sender)

    @Bean
    @Qualifier("send")
    fun sendStep(
            jobRepository: JobRepository,
            platformTransactionManager: PlatformTransactionManager,
            sender: Sender<ProcessedPerson>,
            reader: RepositoryItemReader<ProcessedPerson>,
            sendWriter: SendWriter,
    ): Step = StepBuilder("sendStep", jobRepository)
            .chunk<ProcessedPerson, ProcessedPerson>(10, platformTransactionManager)
            .reader(reader)
            .writer(sendWriter)
            .faultTolerant()
            .retry(CustomRetryException::class.java)
            .retryLimit(3)
            .build()

    @Bean
    @Qualifier("process")
    fun step(
            jobRepository: JobRepository,
            platformTransactionManager: PlatformTransactionManager,
            processor: PersonProcessor,
            writer: SendWriter,
            jpaCursorItemReader: RepositoryItemReader<Person>,
    ): Step = StepBuilder("personStep", jobRepository)
            .chunk<Person, ProcessedPerson>(10, platformTransactionManager)
            .reader(jpaCursorItemReader)
            .processor(processor)
            .writer(writer)
            .faultTolerant()
            .retry(CustomRetryException::class.java)
            .retryLimit(2)
            .build()

    @Bean
    fun job(
            jobRepository: JobRepository,
            @Qualifier("process") process: Step,
            @Qualifier("send") send: Step,
            jobListener: PersonJobListener
    ): Job = JobBuilder("personJob", jobRepository)
            .incrementer(RunIdIncrementer())
            .listener(jobListener)
            .start(process)
            .next(send)
            .build()
}