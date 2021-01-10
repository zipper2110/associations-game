package org.englishclub.associationsgame.backendkotlinspring.controller

import org.englishclub.associationsgame.backendkotlinspring.service.LobbyService
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Controller

@Controller
class LobbyController(private val lobbyService: LobbyService) {

//    @MessageMapping("/hello")
//    @SendTo("topic/greetings")
//    fun example(hello: Hello): Greeting {
//        return Greeting(hello.message)
//    }

    @MessageMapping("/join-lobby")
    fun joinLobby(message: JoinLobbyMessage) {
        val userToken = message.token.toUserDto()
        lobbyService.enterLobby(userToken.id)
    }

    @MessageMapping("/create-room")
    fun createRoom(message: CreateRoomMessage) {
        val user = message.token.toUserDto()
        lobbyService.createRoom(message.roomName, user.id)
    }
}

typealias UserToken = String

open class WebSocketMessage(val token: UserToken)

class JoinLobbyMessage(token: UserToken): WebSocketMessage(token)

class CreateRoomMessage(token: UserToken, val roomName: String): WebSocketMessage(token)
class JoinRoomMessage(token: UserToken, val roomName: String): WebSocketMessage(token)