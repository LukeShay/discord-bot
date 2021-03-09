import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

val springVersion = "5.3.4"
val hibernateVersion = "6.0.0.Alpha6"
val junit5Version = "5.6.0"
val log4jVersion = "2.14.0"
val exposedVersion = "0.29.1"
val kotestVersion = "4.4.3"

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

group = "com.lukeshay.discord"
version = System.getProperty("app.version", "version")

repositories {
    jcenter()
    jcenter {
        url = uri("https://akeyless.jfrog.io/artifactory/akeyless-java")
    }
}

dependencies {
    // Discord dependencies
    implementation("net.dv8tion:JDA:4.2.0_228") {
        exclude("opus-java")
    }

    // Hibernate dependencies
    implementation("org.hibernate.orm:hibernate-core:$hibernateVersion")
    implementation("com.mchange:c3p0:0.9.5.5")

    // Postgres dependencies
    implementation("org.postgresql:postgresql:42.2.19")

    // Kotlin dependencies
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    runtimeOnly("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")

    // klaxon dependencies
    implementation("com.beust:klaxon:5.0.1")

    // Log4j dependencies
    implementation("org.apache.logging.log4j:log4j-api:$log4jVersion")
    implementation("org.apache.logging.log4j:log4j-core:$log4jVersion")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:$log4jVersion")

    // AKEYLESS dependencies
    implementation("io.akeyless:akeyless-java:2.2.1")

    // Exposed dependencies
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jodatime:$exposedVersion")

    // JodaTime dependencies
    implementation("joda-time:joda-time:2.10.10")

    // Sentry dependencies
    implementation("io.sentry:sentry-log4j2:4.3.0")

    // Test dependencies
    // Kotest dependencies
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")

    // Hibernate dependencies
    testImplementation("org.hibernate.orm:hibernate-testing:$hibernateVersion")

    // Mockk dependencies
    testImplementation("io.mockk:mockk:1.10.6")

    // Kotlin dependencies
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.4.2")
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

// tasks.shadowJar {
//    minimize {
//        exclude(dependency(".*:.*:.*"))
//        include(dependency("net.dv8tion:.*:.*"))
//        include(dependency("org.postgresql:postgresql:.*"))
//        include(dependency("org.hibernate.orm:hibernate-core:.*"))
//        include(dependency("com.mchange:c3p0:.*"))
//        include(dependency("io.akeyless:akeyless-java:.*"))
//        include(dependency("org.apache.logging.log4j:.*"))
//        include(dependency("com.beust:klaxon:.*"))
//    }
// }

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
            "worker" to "java \$JAVA_OPTS -Ddatabase.url=\$DATABASE_URL -Denvironment=\$ENVIRONMENT -Dakeyless.access.id=\$AKEYLESS_ACCESS_ID -Dakeyless.access.key=\$AKEYLESS_ACCESS_KEY -jar build/libs/jeffery-krueger-$version-all.jar"
        )
    buildpacks = listOf("heroku/jvm")
    includes = listOf("build/libs/jeffery-krueger-$version-all.jar")
    isIncludeBuildDir = false
}
