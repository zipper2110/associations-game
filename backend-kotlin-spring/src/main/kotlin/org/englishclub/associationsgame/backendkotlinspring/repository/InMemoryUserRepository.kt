package org.englishclub.associationsgame.backendkotlinspring.repository

import org.englishclub.associationsgame.backendkotlinspring.service.*
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

    private val users = mutableListOf<User>()
    private var lastUsersCleanupTime = Instant.now()

    fun userExists(username: String): Boolean {
        cleanupInactiveUsers()

        return users.any { it.username == username }
    }

    fun addUser(user: User) {
        cleanupInactiveUsers()

        if (userExists(user.username)) throw UsernameAlreadyTakenException(user.username)

        users.add(user)
    }

    fun getUserByUsername(username: String): User {
        cleanupInactiveUsers()

        return findUserByUsername(username) ?: throw UserNotFoundException(username)
    }

    fun getUserById(userId: UserId): User {
        cleanupInactiveUsers()

        return findUserById(userId) ?: throw UserNotFoundException(userId = userId)
    }

    fun findUserByUsername(username: String): User? {
        cleanupInactiveUsers()

        return users.firstOrNull { it.username == username }
    }

    fun findUserById(userId: String): User? {
        cleanupInactiveUsers()

        return users.firstOrNull { it.id == userId }
    }

    fun updateStatus(userId: String, status: UserStatus) {
        val user = getUserById(userId)
        users -= user
        users += user.copy(status = status, lastActivityTime = Instant.now())
    }

    fun removeUserById(userId: String) {
        users.filter { it.id == userId }.forEach { users.remove(it) }
    }

    private fun cleanupInactiveUsers() {
        val now = Instant.now()
        if (lastUsersCleanupTime.until(now, SECONDS) < runUserCleanupEverySecs) return

        users.filter { it.lastActivityTime.until(now, SECONDS) > maxAllowedInactivityTimeSec }
            .forEach { users -= it }

        lastUsersCleanupTime = Instant.now()
    }
}