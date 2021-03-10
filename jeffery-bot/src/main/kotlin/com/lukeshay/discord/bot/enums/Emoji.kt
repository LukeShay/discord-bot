package com.lukeshay.discord.bot.enums

enum class Emoji(private val str: String) {
    CRY(":cry:"), PING_PONG(":ping_pong:");

    override fun toString(): String {
        return str
    }
}