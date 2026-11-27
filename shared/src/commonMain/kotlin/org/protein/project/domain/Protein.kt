package org.protein.project.domain

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class Protein(
    val primaryAccession: String,
    val entryName: String,
    val proteinName: String,
    val organismName: String,
    val geneName: String? = null,
    val sequence: String? = null,
    val related: List<RelatedProtein> = emptyList()
)

@Immutable
@Serializable
data class RelatedProtein(
    val accession: String,
    val geneName: String? = null
)

@Immutable
data class ProteinSearchResult(
    val items: List<Protein>,
    val totalFound: Int
)
