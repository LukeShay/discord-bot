package com.lukeshay.discord.listeners.commands

import com.lukeshay.discord.domain.CommandEvent
import com.lukeshay.discord.enums.Environment

class Init(environment: Environment) :
    Command(
        "init",
        "I will setup your server if it hasn't been. You must own the server to run this command.",
        true,
        environment,
        listOf("setup"),
        true,
    ) {
    override fun run(event: CommandEvent) {
        event.reply("This commands is used to setup your server to b")
    }
}