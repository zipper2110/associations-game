package org.englishclub.associationsgame.backendkotlinspring.controller

import org.englishclub.associationsgame.backendkotlinspring.repository.InMemoryUserRepository
import org.englishclub.associationsgame.backendkotlinspring.service.UserStatus.NOWHERE
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Controller

@Controller
class GoNowhereController(private val userRepo: InMemoryUserRepository) {

    @MessageMapping("/go-nowhere")
    fun goNowhere(message: GoNowhereMessage) {
        val userId = message.token.toUserDto().id
        userRepo.updateStatus(userId, NOWHERE)
    }
}

class GoNowhereMessage(token: UserToken): WebSocketMessage(token)