package com.eagskunst.simplepostapp.domain.entity

import java.util.UUID

data class PostEntity(
    val name: String,
    val description: String,
    val added: Boolean = false,
    val id: String = UUID.randomUUID().toString(),
)
