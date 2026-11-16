package org.protein.project.data

import org.protein.project.domain.Protein
import org.protein.project.domain.ProteinRepository

class MockProteinRepositoryImpl(
    private val proteins: List<Protein> = mockProteins
) : ProteinRepository {

    override suspend fun getProteins(query: String): List<Protein> {
        val trimmed = query.trim()
        if (trimmed.isEmpty()) return proteins
        return proteins.filter { protein ->
            protein.proteinName.contains(trimmed, ignoreCase = true) ||
                protein.primaryAccession.contains(trimmed, ignoreCase = true) ||
                protein.entryName.contains(trimmed, ignoreCase = true) ||
                protein.organismName.contains(trimmed, ignoreCase = true) ||
                (protein.geneName?.contains(trimmed, ignoreCase = true) == true)
        }
    }

    override suspend fun getProtein(accession: String): Protein? {
        return proteins.firstOrNull { it.primaryAccession.equals(accession, ignoreCase = true) }
    }
}
