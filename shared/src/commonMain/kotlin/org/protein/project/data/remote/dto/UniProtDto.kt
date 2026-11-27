package org.protein.project.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class UniProtSearchResponseDto(
    val results: List<UniProtEntryDto> = emptyList()
)

@Serializable
data class UniProtEntryDto(
    val primaryAccession: String,
    val uniProtkbId: String = "",
    val proteinDescription: ProteinDescriptionDto? = null,
    val organism: OrganismDto? = null,
    val genes: List<GeneDto> = emptyList(),
    val sequence: SequenceDto? = null,
    val comments: List<CommentDto> = emptyList()
)

@Serializable
data class ProteinDescriptionDto(
    val recommendedName: ProteinNameDto? = null,
    val submissionNames: List<ProteinNameDto> = emptyList(),
    val alternativeNames: List<ProteinNameDto> = emptyList()
)

@Serializable
data class ProteinNameDto(
    val fullName: ValueDto? = null
)

@Serializable
data class ValueDto(
    val value: String
)

@Serializable
data class OrganismDto(
    val scientificName: String? = null,
    val commonName: String? = null
)

@Serializable
data class GeneDto(
    val geneName: ValueDto? = null
)

@Serializable
data class SequenceDto(
    val value: String? = null
)

@Serializable
data class CommentDto(
    val commentType: String? = null,
    val interactions: List<InteractionDto> = emptyList()
)

@Serializable
data class InteractionDto(
    val interactantOne: InteractantDto? = null,
    val interactantTwo: InteractantDto? = null
)

@Serializable
data class InteractantDto(
    val uniProtKBAccession: String? = null,
    val geneName: String? = null
)
