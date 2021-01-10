package org.englishclub.associationsgame.backendkotlinspring.repository

import org.englishclub.associationsgame.backendkotlinspring.service.RoomNotFoundException
import org.englishclub.associationsgame.backendkotlinspring.service.UserId
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class InMemoryRoomsRepository {

    private val rooms = Collections.synchronizedSet(HashSet<Room>())

    fun getByName(roomName: String): Room {
        return findRoomByName(roomName) ?: throw RoomNotFoundException(roomName)
    }

    fun findRoomByName(roomName: String): Room? {
        return rooms.firstOrNull { it.name == roomName }
    }

    fun save(room: Room) {
        rooms.removeIf { it.name == room.name }
        rooms.add(room)
    }

    fun getRoomsList(): Set<Room> {
        return rooms.toSet()
    }
}

data class Room(val name: String, val ownerId: UserId, val participants: Set<String> = emptySet())