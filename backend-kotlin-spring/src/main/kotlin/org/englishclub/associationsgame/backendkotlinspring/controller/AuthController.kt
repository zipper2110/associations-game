package org.englishclub.associationsgame.backendkotlinspring.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.englishclub.associationsgame.backendkotlinspring.service.User
import org.englishclub.associationsgame.backendkotlinspring.service.UserNotFoundException
import org.englishclub.associationsgame.backendkotlinspring.service.UserService
import org.englishclub.associationsgame.backendkotlinspring.service.UsernameAlreadyTakenException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import java.util.*

@RestController
class AuthController(val userService: UserService, val objectMapper: ObjectMapper) {

    @PostMapping("/auth")
    fun auth(@RequestBody request: AuthRequest): AuthResponse {
        return userService.auth(request.username).toAuthResponse()
    }

    @GetMapping("/me")
    fun whoAmI(@RequestParam token: UserToken): User {
        val userId = token.toUserDto().id
        return userService.getById(userId)
    }

    @PostMapping("/logout")
    fun logout(@RequestBody logoutRequest: LogoutRequest) {
        val userToken = logoutRequest.token.toUserDto()
        userService.destroy(userToken.id)
    }

    private fun User.toAuthResponse(): AuthResponse {
        val userDto = UserTokenDto(id, username)
        val userJson = objectMapper.writeValueAsString(userDto)
        val userToken = Base64.getEncoder().encodeToString(userJson.encodeToByteArray())
        return AuthResponse(success = true, token = userToken)
    }

    @ExceptionHandler(UsernameAlreadyTakenException::class, UserNotFoundException::class)
    fun exceptionHandler(e: RuntimeException): ResponseEntity<ErrorResponse> {
        return e.toBadRequestResponse()
    }
}

fun UserToken.toUserDto(): UserTokenDto {
    val objectMapper = ObjectMapper().registerKotlinModule()
    val userTokenJsonString = Base64.getDecoder().decode(this).decodeToString()
    return objectMapper.readValue(userTokenJsonString, UserTokenDto::class.java)
}

data class AuthRequest(val username: String)

data class AuthResponse(val success: Boolean, val token: UserToken)

data class LogoutRequest(val token: UserToken)

data class ErrorResponse(
    val error: String,
    val status: Int,
    val message: String?,
    val timestamp: LocalDateTime = LocalDateTime.now()
)

internal fun Exception.toResponse(status: HttpStatus) =
    ResponseEntity(ErrorResponse(status.reasonPhrase, status.value(), message), status)

internal fun Exception.toBadRequestResponse() = toResponse(HttpStatus.BAD_REQUEST)