package org.protein.project.ui.screens.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.protein.project.domain.Protein
import org.protein.project.resources.Res
import org.protein.project.resources.empty_search_description
import org.protein.project.resources.error_loading
import org.protein.project.resources.found
import org.protein.project.resources.not_found
import org.protein.project.resources.retry
import org.protein.project.ui.components.AppScaffold
import org.protein.project.ui.components.CardSurface
import org.protein.project.ui.components.SearchBar

@Composable
fun ProteinListScreen(
    state: ProteinListState,
    onIntent: (ProteinListIntent) -> Unit,
    onProteinClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    AppScaffold(modifier = modifier) {
        Column(modifier = Modifier.fillMaxSize()) {
            SearchBar(
                query = state.searchQuery,
                onQueryChange = { onIntent(ProteinListIntent.OnSearchQueryChange(it)) },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            when (state) {
                is ProteinListState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is ProteinListState.Error -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = stringResource(Res.string.error_loading),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.error,
                                textAlign = TextAlign.Center
                            )
                            if (!state.message.isNullOrBlank()) {
                                Text(
                                    text = state.message,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Center
                                )
                            }
                            Button(onClick = { onIntent(ProteinListIntent.OnRetry) }) {
                                Text(text = stringResource(Res.string.retry))
                            }
                        }
                    }
                }

                is ProteinListState.Loaded -> {
                    Text(
                        text = stringResource(Res.string.found, state.totalFound),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.bodySmall
                    )

                    if (state.items.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = stringResource(Res.string.not_found),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = stringResource(Res.string.empty_search_description),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(state.items, key = { it.primaryAccession }) { item ->
                                ProteinRow(
                                    protein = item,
                                    onClick = { onProteinClick(item.primaryAccession) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProteinRow(protein: Protein, onClick: () -> Unit) {
    CardSurface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = protein.primaryAccession,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = protein.proteinName, style = MaterialTheme.typography.titleLarge)
            Text(text = protein.organismName, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
