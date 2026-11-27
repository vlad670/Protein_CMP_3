package org.protein.project.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.protein.project.data.AppContainer
import org.protein.project.domain.ProteinRepository

class ProteinDetailViewModel(
    private val accession: String,
    private val repository: ProteinRepository = AppContainer.proteinRepository
) : ViewModel() {

    private val _state = MutableStateFlow<ProteinDetailState>(ProteinDetailState.Loading)
    val state: StateFlow<ProteinDetailState> = _state.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadProtein()
    }

    fun retry() {
        loadProtein()
    }

    private fun loadProtein() {
        loadJob?.cancel()
        _state.value = ProteinDetailState.Loading
        loadJob = viewModelScope.launch {
            try {
                val result = repository.getProtein(accession)
                _state.value = if (result != null) {
                    ProteinDetailState.Loaded(protein = result)
                } else {
                    ProteinDetailState.NotFound
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _state.value = ProteinDetailState.Error(message = e.message)
            }
        }
    }
}
