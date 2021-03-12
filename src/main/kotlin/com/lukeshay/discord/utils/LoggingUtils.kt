package com.lukeshay.discord.utils

import com.lukeshay.discord.enums.Environment
import io.sentry.Sentry
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

object LoggingUtils {
    fun <T> createLogger(clazz: Class<T>): Logger {
        return LogManager.getLogger(clazz)
    }

    fun setupSentry(
        environment: Environment = Environment.determineEnvironment(),
        sentryDsn: String = System.getProperty("sentry.dsn")
            ?: throw Exception("property sentry.dsn is missing"),
        appVersion: String = System.getProperty("app.version")
            ?: throw Exception("property app.version is missing"),
    ) {
        Sentry.init { options ->
            options.dsn = sentryDsn
            options.environment = environment.toString().toLowerCase()
            options.release = appVersion
        }
    }
}