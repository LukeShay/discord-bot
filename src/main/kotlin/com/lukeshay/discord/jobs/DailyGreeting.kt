package com.lukeshay.discord.jobs

import com.lukeshay.discord.entities.GuildConfigs
import com.lukeshay.discord.enums.Emoji
import com.lukeshay.discord.utils.formatQuote
import com.lukeshay.discord.utils.selectAllGuildConfigs
import com.lukeshay.discord.utils.selectOneQuoteByGuildId
import org.apache.logging.log4j.LogManager
import org.springframework.stereotype.Component

@Component
class DailyGreeting : Job("daily greeting") {
    override suspend fun execute() {
        selectAllGuildConfigs().forEach { gc ->
            jda.getTextChannelById(gc[GuildConfigs.defaultChannelId])?.sendMessage(
                selectOneQuoteByGuildId(gc[GuildConfigs.id])?.let { formatQuote(it) }
                    ?: "A quote could not be found ${Emoji.CRY}"
            )?.queue()
                ?: logger.error("could not send message to guild, guild id: ${gc[GuildConfigs.id]}, channel id ${gc[GuildConfigs.defaultChannelId]}")
        }
    }

    companion object {
        private val logger = LogManager.getLogger(DailyGreeting::class.java)
    }
}
