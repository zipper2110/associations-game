package org.englishclub.associationsgame.backendkotlinspring.repository

import org.englishclub.associationsgame.backendkotlinspring.service.User
import org.englishclub.associationsgame.backendkotlinspring.service.UserNotFoundException
import org.englishclub.associationsgame.backendkotlinspring.service.UsernameAlreadyTakenException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import java.time.Instant
import java.time.temporal.ChronoUnit.SECONDS
import java.util.concurrent.ConcurrentHashMap

@Repository
class InMemoryUserRepository(
    @Value("\${users.cleanup-every-secs}")
    private val runUserCleanupEverySecs: Long,

    @Value("\${users.max-user-inactivity-time}")
    private val maxAllowedInactivityTimeSec: Long
) {

    private val users = ConcurrentHashMap<String, User>()
    private var lastUsersCleanupTime = Instant.now()

    fun userExists(username: String): Boolean {
        cleanupInactiveUsers()

        return users.containsKey(username)
    }

    fun addUser(user: User) {
        cleanupInactiveUsers()

        if (userExists(user.username)) throw UsernameAlreadyTakenException(user.username)

        users[user.username] = user
    }

    fun getUser(username: String): User {
        cleanupInactiveUsers()

        return users[username] ?: throw UserNotFoundException(username)
    }

    fun removeUser(userId: String) {
        users.filterValues { it.id == userId }.forEach { users.remove(it.key) }
    }

    private fun cleanupInactiveUsers() {
        val now = Instant.now()
        if (lastUsersCleanupTime.until(now, SECONDS) < runUserCleanupEverySecs) return

        val inactiveUsers = users.values.filter {
            it.lastActivityTime.until(now, SECONDS) > maxAllowedInactivityTimeSec
        }

        inactiveUsers.map { it.username }.forEach { users.remove(it) }

        lastUsersCleanupTime = Instant.now()
    }
}