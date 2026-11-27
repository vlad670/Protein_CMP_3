package org.protein.project.data.remote.mapper

import org.protein.project.data.remote.dto.UniProtEntryDto
import org.protein.project.domain.Protein
import org.protein.project.domain.RelatedProtein

fun UniProtEntryDto.toDomain(): Protein {
    val resolvedName = proteinDescription?.recommendedName?.fullName?.value
        ?: proteinDescription?.submissionNames?.firstOrNull()?.fullName?.value
        ?: proteinDescription?.alternativeNames?.firstOrNull()?.fullName?.value
        ?: uniProtkbId.ifBlank { primaryAccession }

    val scientific = organism?.scientificName
    val common = organism?.commonName
    val resolvedOrganism = when {
        scientific != null && common != null -> "$scientific ($common)"
        scientific != null -> scientific
        common != null -> common
        else -> ""
    }

    val resolvedRelated = comments
        .asSequence()
        .filter { it.commentType == "INTERACTION" }
        .flatMap { it.interactions.asSequence() }
        .mapNotNull { interaction ->
            val other = listOfNotNull(interaction.interactantTwo, interaction.interactantOne)
                .firstOrNull { candidate ->
                    val acc = candidate.uniProtKBAccession
                    !acc.isNullOrBlank() && !acc.equals(primaryAccession, ignoreCase = true)
                }
            other?.uniProtKBAccession?.let { acc ->
                RelatedProtein(
                    accession = acc,
                    geneName = other.geneName
                )
            }
        }
        .distinctBy { it.accession }
        .take(10)
        .toList()

    return Protein(
        primaryAccession = primaryAccession,
        entryName = uniProtkbId.ifBlank { primaryAccession },
        proteinName = resolvedName,
        organismName = resolvedOrganism,
        geneName = genes.firstOrNull()?.geneName?.value,
        sequence = sequence?.value,
        related = resolvedRelated
    )
}
