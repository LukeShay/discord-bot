package com.lukeshay.discord.listeners.commands

import com.lukeshay.discord.domain.CommandEvent
import com.lukeshay.discord.enums.Emoji
import com.lukeshay.discord.enums.Environment

class Ping(environment: Environment) :
    Command("ping", "I will reply with Pong.", true, environment) {
    override fun run(event: CommandEvent) {
        event.sendMessage("Pong ${Emoji.PING_PONG}").queue()
    }
}