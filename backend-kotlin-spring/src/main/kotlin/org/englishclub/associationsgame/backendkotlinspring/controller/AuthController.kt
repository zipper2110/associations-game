package org.englishclub.associationsgame.backendkotlinspring.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.englishclub.associationsgame.backendkotlinspring.service.User
import org.englishclub.associationsgame.backendkotlinspring.service.UserNotFoundException
import org.englishclub.associationsgame.backendkotlinspring.service.UserService
import org.englishclub.associationsgame.backendkotlinspring.service.UsernameAlreadyTakenException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.lang.RuntimeException
import java.time.LocalDateTime
import java.util.*

@RestController
class AuthController(val userService: UserService, val objectMapper: ObjectMapper) {

    @PostMapping("/auth")
    fun auth(@RequestBody request: AuthRequest): AuthResponse {
        return userService.auth(request.username).toAuthResponse()
    }

    @GetMapping("/me")
    fun whoAmI(@RequestParam token: String): User {
        val username = tokenToUser(token).username
        return userService.getByUsername(username)
    }

    @PostMapping("/logout")
    fun logout(@RequestBody logoutRequest: LogoutRequest) {
        val user = tokenToUser(logoutRequest.token)
        userService.destroy(user.id)
    }

    private fun User.toAuthResponse(): AuthResponse {
        val userJson = objectMapper.writeValueAsString(this)
        val userToken = Base64.getEncoder().encodeToString(userJson.encodeToByteArray())
        return AuthResponse(success = true, token = userToken)
    }

    private fun tokenToUser(token: String): User {
        val userJsonString = Base64.getDecoder().decode(token).decodeToString()
        return objectMapper.readValue(userJsonString, User::class.java)
    }

    @ExceptionHandler(UsernameAlreadyTakenException::class, UserNotFoundException::class)
    fun exceptionHandler(e: RuntimeException): ResponseEntity<ErrorResponse> {
        return e.toBadRequestResponse()
    }
}

data class AuthRequest(val username: String)

data class AuthResponse(val success: Boolean, val token: String)

data class LogoutRequest(val token: String)

data class ErrorResponse(
    val error: String,
    val status: Int,
    val message: String?,
    val timestamp: LocalDateTime = LocalDateTime.now()
)

internal fun Exception.toResponse(status: HttpStatus) =
    ResponseEntity(ErrorResponse(status.reasonPhrase, status.value(), message), status)

internal fun Exception.toBadRequestResponse() = toResponse(HttpStatus.BAD_REQUEST)