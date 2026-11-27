package org.protein.project.ui.screens.list

import org.protein.project.domain.Protein

data class ProteinListState(
    val searchQuery: String = "",
    val items: List<Protein> = emptyList(),
    val totalFound: Int = 0
)
