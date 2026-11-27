package org.protein.project

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import org.protein.project.ui.currentLanguage
import org.protein.project.ui.screens.detail.ProteinDetailScreen
import org.protein.project.ui.screens.detail.ProteinDetailViewModel
import org.protein.project.ui.screens.list.ProteinListScreen
import org.protein.project.ui.screens.list.ProteinListViewModel
import org.protein.project.ui.theme.AppTheme

@Composable
fun App() {
    val backStack = remember { mutableStateListOf<Route>(Route.ProteinList) }

    key(currentLanguage) {
        AppTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                NavDisplay(
                    backStack = backStack,
                    onBack = { if (backStack.size > 1) backStack.removeLastOrNull() },
                    transitionSpec = {
                        slideInHorizontally(initialOffsetX = { it }) togetherWith
                            slideOutHorizontally(targetOffsetX = { -it })
                    },
                    popTransitionSpec = {
                        slideInHorizontally(initialOffsetX = { -it }) togetherWith
                            slideOutHorizontally(targetOffsetX = { it })
                    },
                    entryProvider = entryProvider {
                        entry<Route.ProteinList> {
                            val viewModel = viewModel { ProteinListViewModel() }
                            val state by viewModel.state.collectAsStateWithLifecycle()

                            ProteinListScreen(
                                state = state,
                                onIntent = viewModel::onIntent,
                                onProteinClick = { accession ->
                                    backStack.add(Route.ProteinDetail(accession))
                                }
                            )
                        }
                        entry<Route.ProteinDetail> { key ->
                            val viewModel = viewModel(key = key.accession) {
                                ProteinDetailViewModel(key.accession)
                            }
                            val state by viewModel.state.collectAsStateWithLifecycle()

                            ProteinDetailScreen(
                                state = state,
                                onProteinClick = { backStack.add(Route.ProteinDetail(it)) },
                                onBackClick = { if (backStack.size > 1) backStack.removeLastOrNull() },
                                onRetryClick = viewModel::retry
                            )
                        }
                    }
                )
            }
        }
    }
}
