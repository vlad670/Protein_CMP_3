package org.protein.project.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.protein.project.data.remote.dto.UniProtEntryDto
import org.protein.project.data.remote.dto.UniProtSearchResponseDto
import org.protein.project.data.remote.mapper.toDomain
import org.protein.project.domain.Protein
import org.protein.project.domain.ProteinRepository
import org.protein.project.domain.ProteinSearchResult

class UniProtProteinRepositoryImpl(
    private val httpClient: HttpClient,
    private val computeDispatcher: CoroutineDispatcher = Dispatchers.Default
) : ProteinRepository {

    override suspend fun getProteins(query: String): ProteinSearchResult {
        val effectiveQuery = query.trim().ifEmpty { "reviewed:true" }
        val response = httpClient.get("/uniprotkb/search") {
            parameter("query", effectiveQuery)
            parameter("format", "json")
            parameter("size", 25)
        }
        if (!response.status.isSuccess()) {
            throw IllegalStateException("HTTP ${response.status.value}")
        }
        val totalHeader = response.headers["x-total-results"]?.toIntOrNull()
        val rawBody = response.bodyAsText()
        val items = withContext(computeDispatcher) {
            sharedJson.decodeFromString<UniProtSearchResponseDto>(rawBody)
                .results
                .map { it.toDomain() }
        }
        return ProteinSearchResult(
            items = items,
            totalFound = totalHeader ?: items.size
        )
    }

    override suspend fun getProtein(accession: String): Protein? {
        val trimmed = accession.trim()
        val directResult = fetchProteinEntry(trimmed)
        if (directResult != null) return directResult
        if (trimmed.contains("-")) {
            return fetchProteinEntry(trimmed.substringBefore("-"))
        }
        return null
    }

    private suspend fun fetchProteinEntry(accession: String): Protein? {
        val response = httpClient.get("/uniprotkb/$accession.json")
        if (response.status == HttpStatusCode.NotFound) {
            return null
        }
        if (!response.status.isSuccess()) {
            throw IllegalStateException("HTTP ${response.status.value}")
        }
        val rawBody = response.bodyAsText()
        return withContext(computeDispatcher) {
            sharedJson.decodeFromString<UniProtEntryDto>(rawBody).toDomain()
        }
    }
}
