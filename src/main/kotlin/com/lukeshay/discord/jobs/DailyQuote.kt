package com.lukeshay.discord.jobs

import com.lukeshay.discord.logging.createLogger
import com.lukeshay.discord.services.GuildConfigService
import com.lukeshay.discord.services.QuoteService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class DailyQuote @Autowired constructor(
    private val guildConfigService: GuildConfigService,
    private val quoteService: QuoteService
) : Job("daily quote") {
    override suspend fun execute() {
        guildConfigService.findAll().filter { it.dailyQuote }.map { it.defaultChannelId }
            .forEach {
                quoteService.findOne(it)?.let { quote ->
                    jda.getTextChannelById(it)
                        ?.sendMessage("Quote of the day: ${quote.format()}")?.queue()
                } ?: logger.error("there was an error getting a quote")
            }
    }

    companion object {
        private val logger = createLogger(DailyQuote::class.java)
    }
}
