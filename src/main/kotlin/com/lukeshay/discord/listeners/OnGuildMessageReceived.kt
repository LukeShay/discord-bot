package com.lukeshay.discord.listeners

import com.lukeshay.discord.domain.CommandEvent
import com.lukeshay.discord.enums.Emoji
import com.lukeshay.discord.enums.Environment
import com.lukeshay.discord.listeners.commands.Command
import com.lukeshay.discord.listeners.commands.Help
import com.lukeshay.discord.listeners.exceptions.NoCommandRuntimeException
import com.lukeshay.discord.utils.ListenerUtils
import com.lukeshay.discord.utils.LoggingUtils
import com.lukeshay.discord.utils.toJSONString
import io.sentry.Sentry
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class OnGuildMessageReceived(
    cmds: MutableList<Command>,
    private val environment: Environment,
) :
    ListenerAdapter() {

    private var commands: List<Command>

    init {
        cmds.add(Help(cmds.toList(), environment))
        commands = cmds.toList()

        logger.info("available commands -\n\n${cmds.toJSONString(true)}\n")
    }

    override fun onGuildMessageReceived(e: GuildMessageReceivedEvent) {
        ListenerUtils.shouldRun(environment, e)
        val event = CommandEvent(e, environment)

        if (!event.shouldRun) {
            logger.info("${event.guildId}, ${event.authorId} | message is from a bot or not for this env")
            return
        }

        logger.info("${event.guildId}, ${event.authorId} | message - ${event.author.name}: ${event.contentRaw}")

        val command = findCommand(e)

        if (command != null) {
            if (!command.adminOnly || event.authorAsMember?.isOwner == true) {
                logger.info("${event.guildId}, ${event.authorId} | running command - ${command.command}")
                try {
                    command.run(event)
                } catch (e: Throwable) {
                    Sentry.captureException(e)
                    logger.fatal("there us an error running a command $e")
                    event.reply("There was an unexpected error ${Emoji.CRY}").queue()
                }
            } else {
                logger.info("${event.guildId}, ${event.authorId} | unauthorized command - ${command.command}")
                event.reply("You must be an admin to run that command XD").queue()
            }
        } else {
            logger.info("${event.guildId}, ${event.authorId} | no command found for message - ${event.contentRaw}")

            throw NoCommandRuntimeException("no command found for message - ${event.contentRaw}")
        }
    }

    private fun findCommand(event: GuildMessageReceivedEvent): Command? {
        return try {
            commands.first { command ->
                command.matches(
                    event.message.contentRaw.removePrefix(
                        "${
                        environment.toString().toLowerCase()
                        } ",
                    ),
                )
            }
        } catch (e: NoSuchElementException) {
            null
        }
    }

    companion object {
        private val logger = LoggingUtils.createLogger(OnGuildMessageReceived::class.java)
    }
}