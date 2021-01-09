package org.englishclub.associationsgame.backendkotlinspring.service

class UsernameAlreadyTakenException(username: String)
    : RuntimeException("Имя пользователя \"$username\" уже занято")

class UserNotFoundException(username:String)
    : RuntimeException("Пользователь \"$username\" не найден")