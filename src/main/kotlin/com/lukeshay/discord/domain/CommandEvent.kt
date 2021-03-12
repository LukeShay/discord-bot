package com.lukeshay.discord.domain

import com.lukeshay.discord.enums.Environment
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

class CommandEvent(
    val event: GuildMessageReceivedEvent,
    environment: Environment
) {
    val contentRaw =
        event.message.contentRaw.replace(
            "${environment.toString().toLowerCase()} ",
            "",
            ignoreCase = true
        )
    val authorId = event.author.idLong
    val author = event.author
    val guildId = event.guild.idLong
    val guild = event.guild
    val authorAsMember = event.guild.getMember(event.author)
    val forThisEnv = event.message.contentRaw.startsWith(environment.toString().toLowerCase()) || environment == Environment.PRODUCTION
    val shouldRun = !event.author.isBot && forThisEnv

    fun reply(content: CharSequence) = event.message.reply(content)

    fun sendMessage(content: MessageEmbed) = event.channel.sendMessage(content)
    fun sendMessage(content: CharSequence) = event.channel.sendMessage(content)
}