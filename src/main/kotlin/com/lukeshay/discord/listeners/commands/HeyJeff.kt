package com.lukeshay.discord.listeners.commands

import com.lukeshay.discord.domain.CommandEvent
import com.lukeshay.discord.enums.Environment

class HeyJeff(environment: Environment) :
    Command(
        "hey jeff",
        "I will say hi back to you!",
        false,
        environment,
        listOf("hi jeff", "hello jeff")
    ) {
    override fun run(event: CommandEvent) {
        event.reply("Hey").queue()
    }
}