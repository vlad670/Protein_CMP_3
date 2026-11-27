package org.protein.project.domain

interface ProteinRepository {
    suspend fun getProteins(query: String = ""): ProteinSearchResult
    suspend fun getProtein(accession: String): Protein?
}
