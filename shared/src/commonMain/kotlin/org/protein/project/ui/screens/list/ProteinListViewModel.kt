package org.protein.project.ui.screens.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.protein.project.data.MockProteinRepositoryImpl
import org.protein.project.domain.ProteinRepository

class ProteinListViewModel(
    private val repository: ProteinRepository = MockProteinRepositoryImpl()
) : ViewModel() {

    private val _state = MutableStateFlow(ProteinListState())
    val state: StateFlow<ProteinListState> = _state.asStateFlow()

    init {
        loadProteins("")
    }

    fun onIntent(intent: ProteinListIntent) {
        when (intent) {
            is ProteinListIntent.OnSearchQueryChange -> {
                loadProteins(intent.query)
            }
        }
    }

    private fun loadProteins(query: String) {
        viewModelScope.launch {
            val results = repository.getProteins(query)
            _state.value = ProteinListState(
                searchQuery = query,
                items = results,
                totalFound = results.size
            )
        }
    }
}
