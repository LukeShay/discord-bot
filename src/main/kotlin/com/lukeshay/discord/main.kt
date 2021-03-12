package com.lukeshay.discord

import com.lukeshay.discord.enums.Environment
import com.lukeshay.discord.listeners.OnConnectionChange
import com.lukeshay.discord.listeners.OnGuildJoin
import com.lukeshay.discord.listeners.OnGuildMessageReceived
import com.lukeshay.discord.listeners.commands.Bug
import com.lukeshay.discord.listeners.commands.Feature
import com.lukeshay.discord.listeners.commands.HeyJeff
import com.lukeshay.discord.listeners.commands.Init
import com.lukeshay.discord.listeners.commands.Ping
import com.lukeshay.discord.utils.JDAUtils
import com.lukeshay.discord.utils.LoggingUtils
import com.lukeshay.discord.utils.SecretUtils

fun main() {
    val environment = Environment.determineEnvironment()
    SecretUtils.loadSecrets(setOf("discord.token", "sentry.dsn"), environment)
    LoggingUtils.setupSentry(environment)

    JDAUtils.start(
        listeners = listOf(
            OnConnectionChange(),
            OnGuildJoin(environment),
            OnGuildMessageReceived(
                cmds = mutableListOf(
                    Bug(environment),
                    Feature(environment),
                    HeyJeff(environment),
                    Init(environment),
                    Ping(environment),
                ),
                environment = environment,
            ),
        ),
    ).awaitReady()
}