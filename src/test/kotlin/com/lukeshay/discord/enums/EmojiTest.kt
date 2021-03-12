package com.lukeshay.discord.enums

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class EmojiTest : ShouldSpec({
    context("toString") {
        should("return the discord emoji") {
            Emoji.PING_PONG.toString() shouldBe ":ping_pong:"
        }
    }
})