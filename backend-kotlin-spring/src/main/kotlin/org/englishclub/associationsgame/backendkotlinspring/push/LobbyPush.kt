package org.englishclub.associationsgame.backendkotlinspring.push

import org.englishclub.associationsgame.backendkotlinspring.repository.Room
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Component

@Component
class LobbyPush(private val template: SimpMessagingTemplate) {

    fun roomCreated(room: Room) {
        template.convertAndSend("/topic/room-created", room)
    }

    fun sendRoomsList(userId: String, rooms: Set<Room>) {
        val roomsListMessage = RoomsListMessage(rooms.toList())
        template.convertAndSendToUser(userId, "/user/rooms-list", roomsListMessage)
    }
}

data class RoomsListMessage(val rooms: List<Room>)