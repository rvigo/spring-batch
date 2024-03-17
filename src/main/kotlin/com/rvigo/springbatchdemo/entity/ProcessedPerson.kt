package com.rvigo.springbatchdemo.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "processed_person")
data class ProcessedPerson(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Int
) {

    @Column
    lateinit var name: String

    @Column
    @Enumerated(EnumType.STRING)
    val status: Status = Status.PROCESSED

    constructor(
            id: Int = 0,
            name: String,

    ) : this(id) {
        this.name = name
    }
}
