package com.lukeshay.discord.enums

import net.dv8tion.jda.api.entities.Guild

enum class Environment {
    LOCAL, DEVELOPMENT, STAGING, PRODUCTION;

    companion object {
        fun determineEnvironment(): Environment {
            val env = System.getProperty("environment", "local")

            return when (env.toLowerCase()) {
                "production" -> PRODUCTION
                "staging" -> STAGING
                "development" -> DEVELOPMENT
                else -> LOCAL
            }
        }
    }

    /**
     * Returns a list of the IDs of guilds the environment can be used in.
     */
    fun allowedGuilds(): List<String>? {
        return when (this) {
            LOCAL -> listOf("816344932414521355")
            DEVELOPMENT -> listOf("816344932414521355")
            STAGING -> listOf("816344932414521355")
            PRODUCTION -> null
        }
    }

    /**
     * Checks if the environment is allowed in the given Guild.
     */
    fun isAllowed(guild: Guild?): Boolean {
        return guild != null && (
            allowedGuilds() == null || allowedGuilds()?.contains(
                guild.id
            ) == true
            )
    }
}