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
@Table(name = "person")
data class Person(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Int
) {

    @Column
    lateinit var name: String

    @Column
    @Enumerated(EnumType.STRING)
    lateinit var status: Status



    constructor(
            id: Int = 0,
            name: String,
            status: Status

    ) : this(id) {
        this.name = name
        this.status = status
    }
}