package org.englishclub.associationsgame.backendkotlinspring.service

import org.englishclub.associationsgame.backendkotlinspring.push.RoomPush
import org.englishclub.associationsgame.backendkotlinspring.repository.InMemoryRoomsRepository
import org.englishclub.associationsgame.backendkotlinspring.repository.InMemoryUserRepository
import org.englishclub.associationsgame.backendkotlinspring.service.UserStatus.IN_GAME_ROOM
import org.springframework.stereotype.Service

@Service
class RoomService(
    private val roomsRepo: InMemoryRoomsRepository,
    private val userRepo: InMemoryUserRepository,
    private val roomPush: RoomPush,
) {

    fun joinRoom(userId: UserId, roomName: String) {
        val room = roomsRepo.getByName(roomName)
        val roomWithUser = room.copy(participants = room.participants + userId)
        roomsRepo.save(roomWithUser)
        userRepo.updateStatus(userId, IN_GAME_ROOM)

        roomPush.playerJoinedRoom(roomWithUser, userId, "who knows...")
        roomPush.sendRoomState(userId, roomWithUser)
    }
}