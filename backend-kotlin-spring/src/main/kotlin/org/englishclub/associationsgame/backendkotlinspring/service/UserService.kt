package org.englishclub.associationsgame.backendkotlinspring.service

import org.englishclub.associationsgame.backendkotlinspring.repository.InMemoryUserRepository
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepo: InMemoryUserRepository) {

    fun auth(username: String): User {
        if (userRepo.userExists(username)) throw UsernameAlreadyTakenException(username)

        val user = User(username = username)
        userRepo.addUser(user)
        return user
    }

    fun destroy(userId: String) {
        userRepo.removeUserById(userId)
    }

    fun getByUsername(username: String): User {
        return userRepo.getUserByUsername(username)
    }

    fun getById(userId: UserId): User {
        return userRepo.getUserById(userId)
    }
}

