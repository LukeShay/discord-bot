import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

val aKeylessVersion: String by project
val jdaVersion: String by project
val kotestVersion: String by project
val log4jVersion: String by project
val mockkVersion: String by project
val sentryVersion: String by project
val shadowVersion: String by project

buildscript {
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-allopen:1.4.31")
    }
}

plugins {
    application
    jacoco
    id("org.jetbrains.kotlin.jvm") version "1.4.31"
    id("org.jlleitschuh.gradle.ktlint") version "10.0.0"
    id("com.heroku.sdk.heroku-gradle") version "2.0.0"
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

version = System.getProperty("app.version", "version")

repositories {
    jcenter()
    jcenter {
        url = uri("https://akeyless.jfrog.io/artifactory/akeyless-java")
    }
}

dependencies {
    // Discord dependencies
    implementation("net.dv8tion:JDA:$jdaVersion") {
        exclude("opus-java")
    }

    // Kotlin dependencies
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    runtimeOnly("org.jetbrains.kotlin:kotlin-reflect")

    // Log4j dependencies
    implementation("org.apache.logging.log4j:log4j-api:$log4jVersion")
    implementation("org.apache.logging.log4j:log4j-core:$log4jVersion")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:$log4jVersion")

    // AKEYLESS dependencies
    implementation("io.akeyless:akeyless-java:$aKeylessVersion")

    // Sentry dependencies
    implementation("io.sentry:sentry-log4j2:$sentryVersion")

    // Test dependencies
    // Kotest dependencies
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")

    // Mockk dependencies
    testImplementation("io.mockk:mockk:$mockkVersion")
}

tasks.jacocoTestReport {
    // Adjust the output of the test report
    reports {
        xml.isEnabled = true
        html.isEnabled = true
        csv.isEnabled = true
        html.destination = file("$buildDir/jacocoHtml")
    }
}

tasks.jacocoTestCoverageVerification {
    classDirectories.setFrom(
        files(
            classDirectories.files.map {
                fileTree(
                    mapOf(
                        "dir" to it,
                        "exclude" to arrayOf(
                            "com/lukeshay/discord/entities/**",
                            "com/lukeshay/discord/logging/**",
                            "com/lukeshay/discord/MainKt.class"
                        )
                    )
                )
            }
        )
    )

    violationRules {
        rule {
            isFailOnViolation = false
            element = "BUNDLE"
            limit {
                minimum = "0.7".toBigDecimal()
            }
        }
        rule {
            isFailOnViolation = false
            element = "SOURCEFILE"
            limit {
                minimum = "0.01".toBigDecimal()
            }
        }
    }
}

fun passSystemProperties(jfo: JavaForkOptions) {
    jfo.systemProperty("database.url", System.getProperty("database.url"))
    jfo.systemProperty("environment", System.getProperty("environment"))
    jfo.systemProperty("akeyless.access.id", System.getProperty("akeyless.access.id"))
    jfo.systemProperty("akeyless.access.key", System.getProperty("akeyless.access.key"))
}

tasks.withType<Test> {
    useJUnitPlatform()

    passSystemProperties(this)

    finalizedBy(tasks.jacocoTestReport)
    finalizedBy(tasks.jacocoTestCoverageVerification)
}

tasks.runShadow {
    passSystemProperties(this)
}

application {
    mainClass.set("com.lukeshay.discord.MainKt")
    mainClassName = "com.lukeshay.discord.MainKt"
}

ktlint {
    verbose.set(true)
    outputToConsole.set(true)
    coloredOutput.set(true)
    reporters {
        reporter(ReporterType.CHECKSTYLE)
    }
    filter {
        exclude("**/style-violations.kt")
    }
}

heroku {
    appName = System.getProperty("heroku.app.name", "jeffery-krueger-dev")
    jdkVersion = "11"
    processTypes =
        mapOf(
            "worker" to "java \$JAVA_OPTS " +
                "-Ddatabase.url=\$DATABASE_URL " +
                "-Denvironment=\$ENVIRONMENT " +
                "-Dakeyless.access.id=\$AKEYLESS_ACCESS_ID " +
                "-Dakeyless.access.key=\$AKEYLESS_ACCESS_KEY " +
                "-Dapp.version=$version" +
                "-jar build/libs/jeffery-krueger-$version-all.jar"
        )
    buildpacks = listOf("heroku/jvm")
    includes = listOf("build/libs/jeffery-krueger-$version-all.jar")
    isIncludeBuildDir = false
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}