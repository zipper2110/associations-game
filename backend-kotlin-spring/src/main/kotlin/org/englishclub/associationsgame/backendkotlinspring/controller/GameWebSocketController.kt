package org.englishclub.associationsgame.backendkotlinspring.controller

import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller

@Controller
class GameWebSocketController {

    @MessageMapping("/hello")
    @SendTo("topic/greetings")
    fun example(hello: Hello): Greeting {
        return Greeting(hello.message)
    }
}

data class Hello(val message: String)
data class Greeting(val message: String)