package com.rvigo.springbatchdemo.repository

import com.rvigo.springbatchdemo.entity.Person
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PersonRepository : JpaRepository<Person, Int>