package com.lukeshay.discord.utils

import com.lukeshay.discord.enums.Environment
import io.akeyless.client.api.V2Api
import io.akeyless.client.model.Configure
import io.akeyless.client.model.GetSecretValue

object SecretUtils {
    fun loadSecrets(
        properties: Set<String>,
        environment: Environment = Environment.determineEnvironment(),
        aKeylessAccessId: String = System.getProperty("akeyless.access.id")
            ?: throw Exception("akeyless.access.id not found"),
        aKeylessAccesskey: String = System.getProperty("akeyless.access.key")
            ?: throw Exception("akeyless.access.id not found")
    ) {
        val secretsMap: MutableMap<String, String> = mutableMapOf()

        properties.forEach { secretsMap[it] = it.replace(".", "-") }

        val apiClient = io.akeyless.client.Configuration.getDefaultApiClient()

        apiClient.basePath = "https://api.akeyless.io"

        val v2Api = V2Api(apiClient)

        val aKeylessToken = v2Api.configure(Configure().accessId(aKeylessAccessId).accessKey(aKeylessAccesskey)).token
            ?: throw Exception("error getting v2 api token")

        val secretsPath = "jeffery-krueger/${environment.toString().toLowerCase()}"

        val body = GetSecretValue().token(aKeylessToken)

        secretsMap.forEach { (_, s) -> body.addNamesItem("$secretsPath/$s") }

        val result = v2Api.getSecretValue(body)

        secretsMap.forEach { (p, s) -> System.setProperty(p, result["$secretsPath/$s"]!!) }
    }
}