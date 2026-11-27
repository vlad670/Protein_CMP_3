package org.protein.project.ui.screens.detail

import androidx.compose.runtime.Immutable
import org.protein.project.domain.Protein

@Immutable
sealed class ProteinDetailState {
    @Immutable
    data object Loading : ProteinDetailState()

    @Immutable
    data class Loaded(
        val protein: Protein
    ) : ProteinDetailState()

    @Immutable
    data object NotFound : ProteinDetailState()

    @Immutable
    data class Error(
        val message: String? = null
    ) : ProteinDetailState()
}
