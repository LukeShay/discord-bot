package com.lukeshay.discord.listeners

import com.lukeshay.discord.enums.Environment
import com.lukeshay.discord.utils.ListenerUtils
import com.lukeshay.discord.utils.LoggingUtils
import net.dv8tion.jda.api.events.guild.GuildJoinEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class OnGuildJoin(private val environment: Environment) :
    ListenerAdapter() {
    override fun onGuildJoin(event: GuildJoinEvent) {
        ListenerUtils.shouldRun(environment, event)
        logger.info("joined guild - ${event.guild.id}")

        event.guild
            .defaultChannel
            ?.sendMessage("Thank you for adding me to your server! Send the message '!help' for information on my commands.")
            ?.queue()
    }

    companion object {
        private val logger = LoggingUtils.createLogger(OnGuildJoin::class.java)
    }
}