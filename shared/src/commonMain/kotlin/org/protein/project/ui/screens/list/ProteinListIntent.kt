package org.protein.project.ui.screens.list

sealed interface ProteinListIntent {
    data class OnSearchQueryChange(val query: String) : ProteinListIntent
}
