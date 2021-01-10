package org.englishclub.associationsgame.backendkotlinspring.controller

import org.englishclub.associationsgame.backendkotlinspring.service.UserId

data class UserTokenDto(val id: UserId, val username: String)