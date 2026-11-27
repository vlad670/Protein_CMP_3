package org.protein.project.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.protein.project.data.MockProteinRepositoryImpl
import org.protein.project.domain.ProteinRepository

class ProteinDetailViewModel(
    private val accession: String,
    private val repository: ProteinRepository = MockProteinRepositoryImpl()
) : ViewModel() {

    private val _state = MutableStateFlow(ProteinDetailState())
    val state: StateFlow<ProteinDetailState> = _state.asStateFlow()

    init {
        loadProtein()
    }

    private fun loadProtein() {
        viewModelScope.launch {
            val result = repository.getProtein(accession)
            _state.value = ProteinDetailState(protein = result)
        }
    }
}
