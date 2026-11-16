package org.protein.project.domain

interface ProteinRepository {
    suspend fun getProteins(query: String = ""): List<Protein>
    suspend fun getProtein(accession: String): Protein?
}
