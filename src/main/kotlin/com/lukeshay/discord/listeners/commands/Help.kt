package com.lukeshay.discord.listeners.commands

import com.lukeshay.discord.domain.CommandEvent
import com.lukeshay.discord.enums.Environment
import java.awt.Color
import net.dv8tion.jda.api.EmbedBuilder

class Help(
    private val commands: List<Command>,
    environment: Environment,
) :
    Command(
        "help",
        "Displays this message.",
        true,
        environment,
        listOf("options")
    ) {

    override fun run(event: CommandEvent) {
        sendHelpMessage(
            event,
            false,
            "Available Commands",
            "Hey! My name is Jeffrey Krueger. Here is a list of what I am capable of."
        )

        if (event.authorAsMember?.isOwner == true) {
            sendHelpMessage(
                event,
                true,
                "Admin Only Commands",
                "This is a list of commands only admins can run.",
                Color.ORANGE
            )
        }
    }

    private fun sendHelpMessage(
        event: CommandEvent,
        adminOnly: Boolean,
        title: String,
        description: String,
        color: Color = Color.GREEN
    ) {
        val msgEmbedBuilder = EmbedBuilder().setTitle(title).setColor(color)
            .setFooter("All of my commands are case insensitive. That means !HeLp will work.")
        msgEmbedBuilder.descriptionBuilder.append(description)

        commands.filter { it.adminOnly == adminOnly }.sortedBy { it.command }.forEach {
            msgEmbedBuilder.addField(it.command, it.description, false)
        }

        msgEmbedBuilder.addField("!help", description, false)

        if (environment != Environment.PRODUCTION) {
            msgEmbedBuilder.addField("environment", environment.toString(), true)
        }

        event.sendMessage(msgEmbedBuilder.build()).queue()
    }
}