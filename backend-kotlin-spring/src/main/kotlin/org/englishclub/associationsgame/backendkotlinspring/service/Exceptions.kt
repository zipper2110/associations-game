package org.englishclub.associationsgame.backendkotlinspring.service

class UsernameAlreadyTakenException(username: String)
    : RuntimeException("Имя пользователя \"$username\" уже занято")

class UserNotFoundException(username: String = "<unknown>", userId: String = "<unknown>")
    : RuntimeException("Пользователь с именем \"$username\" и id \"$userId\" не найден")

class RoomNotFoundException(roomName: String): RuntimeException("Комната \"$roomName\" не найдена")

class RoomAlreadyExistsException(roomName: String): RuntimeException("Комната \"$roomName\" уже существует")