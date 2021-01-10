package org.englishclub.associationsgame.backendkotlinspring.push

import org.englishclub.associationsgame.backendkotlinspring.repository.Room
import org.englishclub.associationsgame.backendkotlinspring.service.UserId
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Component

@Component
class RoomPush(private val template: SimpMessagingTemplate) {

    fun sendRoomState(userId: String, room: Room) {
        template.convertAndSendToUser(userId, "/user/rooms-state", room)
    }

    fun playerJoinedRoom(room: Room, playerId: UserId, playerUsername: String) {
        template.convertAndSend("/topic/player-joined-room", room)
    }
}