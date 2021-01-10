package org.englishclub.associationsgame.backendkotlinspring.service

import org.englishclub.associationsgame.backendkotlinspring.push.LobbyPush
import org.englishclub.associationsgame.backendkotlinspring.repository.InMemoryRoomsRepository
import org.englishclub.associationsgame.backendkotlinspring.repository.InMemoryUserRepository
import org.englishclub.associationsgame.backendkotlinspring.repository.Room
import org.englishclub.associationsgame.backendkotlinspring.service.UserStatus.IN_LOBBY
import org.springframework.stereotype.Service

@Service
class LobbyService(private val lobbyPush: LobbyPush,
                   private val roomsRepo: InMemoryRoomsRepository,
                   private val usersRepo: InMemoryUserRepository,
) {

    fun enterLobby(userId: String) {
        val roomsList = roomsRepo.getRoomsList()
        lobbyPush.sendRoomsList(userId, roomsList)
        usersRepo.updateStatus(userId, IN_LOBBY)
    }

    fun createRoom(roomName: String, ownerId: UserId) {
        roomsRepo.findRoomByName(roomName)?.let {
            throw RoomAlreadyExistsException(roomName) // TODO: send message to client
        }
        val room = Room(roomName, ownerId)
        roomsRepo.save(room)

        lobbyPush.roomCreated(room)
    }
}