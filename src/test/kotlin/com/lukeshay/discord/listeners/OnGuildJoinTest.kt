package com.lukeshay.discord.listeners

import com.lukeshay.discord.enums.Environment
import io.kotest.core.spec.style.ShouldSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.time.Instant
import kotlin.random.Random
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.events.guild.GuildJoinEvent
import net.dv8tion.jda.api.requests.restaction.MessageAction

class OnGuildJoinTest : ShouldSpec({
    val random = Random(Instant.now().toEpochMilli())
    val guildId = random.nextLong()
    val ownerId = random.nextLong()

    val guildMock = mockk<Guild>(relaxed = true)
    val guildEventMock = mockk<GuildJoinEvent>(relaxed = true)
    val textChannelMock = mockk<TextChannel>(relaxed = true)
    val messageActionMock = mockk<MessageAction>(relaxed = true)

    beforeTest {
        every { textChannelMock.sendMessage(any<String>()) } returns messageActionMock

        every { guildMock.idLong } returns guildId
        every { guildMock.id } returns guildId.toString()
        every { guildMock.ownerIdLong } returns ownerId
        every { guildMock.defaultChannel } returns textChannelMock

        every { guildEventMock.guild } returns guildMock
    }

    context("onGuildJoin") {
        should("send initial message") {
            OnGuildJoin(Environment.PRODUCTION).onGuildJoin(guildEventMock)

            verify {
                textChannelMock
                    .sendMessage("Thank you for adding me to your server! Send the message '!help' for information on my commands.")
                messageActionMock.queue()
            }
        }
    }
})