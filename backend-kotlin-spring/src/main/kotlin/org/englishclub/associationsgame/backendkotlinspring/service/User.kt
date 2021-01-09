package org.englishclub.associationsgame.backendkotlinspring.service

import java.time.Instant
import java.util.*

data class User(
    val id: String = UUID.randomUUID().toString(),
    val username: String,
    val lastActivityTime: Instant = Instant.now()
)