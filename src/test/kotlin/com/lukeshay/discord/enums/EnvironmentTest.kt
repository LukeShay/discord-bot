package com.lukeshay.discord.enums

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class EnvironmentTest : ShouldSpec({
    context("determineEnv") {
        should("return local") {
            val original = System.getProperty("environment", "local")
            System.setProperty("environment", "local")

            Environment.determineEnvironment() shouldBe Environment.LOCAL

            System.setProperty("environment", original)
        }

        should("return development") {
            val original = System.getProperty("environment", "development")
            System.setProperty("environment", "development")

            Environment.determineEnvironment() shouldBe Environment.DEVELOPMENT

            System.setProperty("environment", original)
        }

        should("return staging") {
            val original = System.getProperty("environment", "staging")
            System.setProperty("environment", "staging")

            Environment.determineEnvironment() shouldBe Environment.STAGING

            System.setProperty("environment", original)
        }

        should("return production") {
            val original = System.getProperty("environment", "production")
            System.setProperty("environment", "production")

            Environment.determineEnvironment() shouldBe Environment.PRODUCTION

            System.setProperty("environment", original)
        }
    }
})