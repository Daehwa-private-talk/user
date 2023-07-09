package com.daehwa.user.model

import com.daehwa.user.model.base_entity.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    val email: String,
    val password: String,
    val name: String,
    val nickname: String,
    @Column(name = "is_enabled", columnDefinition = "TINYINT")
    val enabled: Boolean
) : BaseEntity()
