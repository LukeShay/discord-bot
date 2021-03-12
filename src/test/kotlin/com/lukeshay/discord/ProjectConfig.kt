package com.lukeshay.discord

import io.kotest.core.config.AbstractProjectConfig

object ProjectConfig : AbstractProjectConfig() {
    override val parallelism = 4
    override val failOnIgnoredTests = true
}