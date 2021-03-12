package com.lukeshay.discord.listeners.commands

import com.lukeshay.discord.domain.CommandEvent
import com.lukeshay.discord.enums.Environment
import io.kotest.core.spec.style.ShouldSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import net.dv8tion.jda.api.requests.restaction.MessageAction
import org.junit.jupiter.api.Assertions

@ExperimentalCoroutinesApi
internal class HeyJeffTest : ShouldSpec(
    {
        val heyJeff = HeyJeff(Environment.determineEnvironment())

        context("constructor") {
            should("set correct values") {

                Assertions.assertEquals("hey jeff", heyJeff.command)
                Assertions.assertEquals(2, heyJeff.aliases.size)
            }
        }

        context("run") {
            val eventMock = mockk<CommandEvent>(relaxed = true)
            val messageActionMock = mockk<MessageAction>(relaxed = true)

            beforeEach {
                every { eventMock.guildId } returns 0
                every { eventMock.reply(any()) } returns messageActionMock
            }

            should("reply") {
                heyJeff.run(eventMock)

                verify {
                    eventMock.reply(any())
                    messageActionMock.queue()
                }
            }
        }
    },
)