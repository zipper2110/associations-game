package org.englishclub.associationsgame.backendkotlinspring.service

import org.englishclub.associationsgame.backendkotlinspring.service.UserStatus.NOWHERE
import java.time.Instant
import java.util.*

data class User(
    val id: UserId = UUID.randomUUID().toString(),
    val username: String,
    val status: UserStatus = NOWHERE,
    val lastActivityTime: Instant = Instant.now()
)

typealias UserId = String

enum class UserStatus {
    NOWHERE,
    IN_LOBBY,
    IN_GAME_ROOM,
    IN_GAME
}