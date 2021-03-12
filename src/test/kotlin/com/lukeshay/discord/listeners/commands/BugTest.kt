package com.lukeshay.discord.listeners.commands

import com.lukeshay.discord.enums.Environment
import io.kotest.core.spec.style.ShouldSpec
import org.junit.jupiter.api.Assertions

internal class BugTest : ShouldSpec({
    context("constructor") {
        should("set correct values") {
            val bug = Bug(Environment.determineEnvironment())

            Assertions.assertEquals("!bug", bug.command)
            Assertions.assertEquals(0, bug.aliases.size)
        }
    }
})