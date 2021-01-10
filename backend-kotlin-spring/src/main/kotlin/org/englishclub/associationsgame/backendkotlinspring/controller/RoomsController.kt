package org.englishclub.associationsgame.backendkotlinspring.controller

import org.englishclub.associationsgame.backendkotlinspring.repository.InMemoryRoomsRepository
import org.englishclub.associationsgame.backendkotlinspring.service.RoomService
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Controller

@Controller
class RoomsController(private val roomService: RoomService) {

    @MessageMapping("/join-room")
    fun joinRoom(message: JoinRoomMessage) {
        val userDto = message.token.toUserDto()
        roomService.joinRoom(userDto.id, message.roomName)
    }

    fun kickPlayer() {

    }

    fun startGame() {

    }

    fun destroyRoom() {

    }

    fun ready() {

    }

    fun notReady() {

    }

    fun sendMessage() {

    }
}

enum class UserRoleInRoom {
    ADMIN,
    PLAYER
}