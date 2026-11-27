package org.protein.project.data

import org.protein.project.domain.Protein
import org.protein.project.domain.ProteinRepository
import org.protein.project.domain.ProteinSearchResult

class MockProteinRepositoryImpl(
    private val proteins: List<Protein> = mockProteins
) : ProteinRepository {

    override suspend fun getProteins(query: String): ProteinSearchResult {
        val trimmed = query.trim()
        val filtered = if (trimmed.isEmpty()) {
            proteins
        } else {
            proteins.filter { protein ->
                protein.proteinName.contains(trimmed, ignoreCase = true) ||
                    protein.primaryAccession.contains(trimmed, ignoreCase = true) ||
                    protein.entryName.contains(trimmed, ignoreCase = true) ||
                    protein.organismName.contains(trimmed, ignoreCase = true) ||
                    (protein.geneName?.contains(trimmed, ignoreCase = true) == true)
            }
        }
        return ProteinSearchResult(items = filtered, totalFound = filtered.size)
    }

    override suspend fun getProtein(accession: String): Protein? {
        return proteins.firstOrNull { it.primaryAccession.equals(accession, ignoreCase = true) }
    }
}
