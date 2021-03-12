package com.lukeshay.discord.utils

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.hooks.ListenerAdapter

object JDAUtils {
    fun jdaBuilder(): JDABuilder {
        val builder = JDABuilder.createDefault(System.getProperty("discord.token"))

        builder.setAutoReconnect(true)
        builder.setStatus(OnlineStatus.ONLINE)

        return builder
    }

    fun start(
        listeners: List<ListenerAdapter>,
        builder: JDABuilder = jdaBuilder(),
    ): JDA {
        for (listener in listeners) {
            builder.addEventListeners(listener)
        }

        return builder.build()
    }
}