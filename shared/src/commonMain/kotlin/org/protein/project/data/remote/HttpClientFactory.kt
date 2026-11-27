package org.protein.project.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.protein.project.config.ApiConfig

expect fun createPlatformHttpClient(block: HttpClientConfig<*>.() -> Unit): HttpClient

val sharedJson = Json {
    ignoreUnknownKeys = true
    isLenient = true
}

fun createUniProtHttpClient(): HttpClient {
    require(ApiConfig.BASE_URL.isNotBlank()) {
        "uniprot.baseUrl is not configured in local.properties"
    }
    return createPlatformHttpClient {
        install(ContentNegotiation) {
            json(sharedJson)
        }
        defaultRequest {
            url(ApiConfig.BASE_URL)
        }
    }
}
