package org.protein.project.ui.screens.list

import androidx.compose.runtime.Immutable
import org.protein.project.domain.Protein

@Immutable
sealed class ProteinListState {
    abstract val searchQuery: String

    @Immutable
    data class Loading(
        override val searchQuery: String = ""
    ) : ProteinListState()

    @Immutable
    data class Loaded(
        override val searchQuery: String,
        val items: List<Protein>,
        val totalFound: Int
    ) : ProteinListState()

    @Immutable
    data class Error(
        override val searchQuery: String,
        val message: String? = null
    ) : ProteinListState()
}
