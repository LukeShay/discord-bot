package com.lukeshay.discord.listeners.commands

import com.lukeshay.discord.enums.Environment
import io.kotest.core.spec.style.ShouldSpec
import org.junit.jupiter.api.Assertions

internal class VerbTest : ShouldSpec({
    context("constructor") {
        should("set correct values") {
            val verb = Verb(Environment.determineEnvironment())

            Assertions.assertEquals("!verb", verb.command)
            Assertions.assertEquals(0, verb.aliases.size)
        }
    }
})
