package org.protein.project.ui.screens.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.protein.project.data.AppContainer
import org.protein.project.domain.ProteinRepository

class ProteinListViewModel(
    private val repository: ProteinRepository = AppContainer.proteinRepository
) : ViewModel() {

    private val _state = MutableStateFlow<ProteinListState>(ProteinListState.Loading())
    val state: StateFlow<ProteinListState> = _state.asStateFlow()

    private var searchJob: Job? = null

    init {
        loadProteins(query = "", debounceMillis = 0L)
    }

    fun onIntent(intent: ProteinListIntent) {
        when (intent) {
            is ProteinListIntent.OnSearchQueryChange -> {
                loadProteins(query = intent.query, debounceMillis = 350L)
            }
            ProteinListIntent.OnRetry -> {
                loadProteins(query = _state.value.searchQuery, debounceMillis = 0L)
            }
        }
    }

    private fun loadProteins(query: String, debounceMillis: Long) {
        searchJob?.cancel()
        _state.value = ProteinListState.Loading(searchQuery = query)
        searchJob = viewModelScope.launch {
            if (debounceMillis > 0L) {
                delay(debounceMillis)
            }
            try {
                val result = repository.getProteins(query)
                _state.value = ProteinListState.Loaded(
                    searchQuery = query,
                    items = result.items,
                    totalFound = result.totalFound
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _state.value = ProteinListState.Error(
                    searchQuery = query,
                    message = e.message
                )
            }
        }
    }
}
