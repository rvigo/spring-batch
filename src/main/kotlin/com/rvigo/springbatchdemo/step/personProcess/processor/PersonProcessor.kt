package com.rvigo.springbatchdemo.step.personProcess.processor

import com.rvigo.springbatchdemo.CustomRetryException
import com.rvigo.springbatchdemo.entity.Person
import com.rvigo.springbatchdemo.entity.ProcessedPerson
import com.rvigo.springbatchdemo.logger
import org.springframework.batch.item.ItemProcessor

class PersonProcessor : ItemProcessor<Person, ProcessedPerson> {
    private val log = logger()

    override fun process(item: Person): ProcessedPerson {
//        if (item.id == 7) {
//            log.warn("found 7")
//            throw CustomRetryException()
//        }
        return item.processPerson()
    }

    private fun Person.processPerson(): ProcessedPerson = ProcessedPerson(this.id, this.name)
}