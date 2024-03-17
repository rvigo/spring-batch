package com.rvigo.springbatchdemo.repository

import com.rvigo.springbatchdemo.entity.ProcessedPerson
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProcessedPersonRepository : JpaRepository<ProcessedPerson, Int>