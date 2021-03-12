package com.lukeshay.discord.listeners

import com.lukeshay.discord.utils.LoggingUtils
import net.dv8tion.jda.api.events.DisconnectEvent
import net.dv8tion.jda.api.events.ExceptionEvent
import net.dv8tion.jda.api.events.ReconnectedEvent
import net.dv8tion.jda.api.events.ShutdownEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class OnConnectionChange : ListenerAdapter() {
    override fun onReconnected(event: ReconnectedEvent) {
        super.onReconnected(event)
        logger.warn("reconnected")
    }

    override fun onDisconnect(event: DisconnectEvent) {
        super.onDisconnect(event)
        logger.warn("disconnected ${event.closeCode}")
    }

    override fun onShutdown(event: ShutdownEvent) {
        super.onShutdown(event)
        logger.warn("shutdown ${event.closeCode}")
    }

    override fun onException(event: ExceptionEvent) {
        super.onException(event)
        logger.warn("exception ${event.cause}")
    }

    companion object {
        private val logger = LoggingUtils.createLogger(OnConnectionChange::class.java)
    }
}