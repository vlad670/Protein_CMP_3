package org.protein.project.ui.screens.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.protein.project.domain.Protein
import org.protein.project.resources.Res
import org.protein.project.resources.amino_acid_sequence
import org.protein.project.resources.back
import org.protein.project.resources.error_loading
import org.protein.project.resources.full_name
import org.protein.project.resources.gene
import org.protein.project.resources.not_found
import org.protein.project.resources.organism
import org.protein.project.resources.related_proteins
import org.protein.project.resources.retry
import org.protein.project.resources.unknown
import org.protein.project.ui.components.AppScaffold

@Composable
fun ProteinDetailScreen(
    state: ProteinDetailState,
    onProteinClick: (String) -> Unit,
    onBackClick: () -> Unit,
    onRetryClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    AppScaffold(
        modifier = modifier,
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(Res.string.back)
                )
            }
        }
    ) {
        when (state) {
            ProteinDetailState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            ProteinDetailState.NotFound -> {
                Text(
                    text = stringResource(Res.string.not_found),
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            is ProteinDetailState.Error -> {
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
                        Button(onClick = onRetryClick) {
                            Text(text = stringResource(Res.string.retry))
                        }
                    }
                }
            }

            is ProteinDetailState.Loaded -> {
                ProteinDetailContent(
                    protein = state.protein,
                    onProteinClick = onProteinClick
                )
            }
        }
    }
}

@Composable
fun ProteinDetailContent(
    protein: Protein,
    onProteinClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = protein.entryName,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(Res.string.full_name),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )
        Text(text = protein.proteinName, style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(Res.string.organism),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )
        Text(text = protein.organismName, style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(Res.string.gene),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )
        Text(
            text = protein.geneName ?: stringResource(Res.string.unknown),
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(Res.string.amino_acid_sequence),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )
        Text(
            text = protein.sequence ?: stringResource(Res.string.unknown),
            style = MaterialTheme.typography.bodyMedium,
            fontFamily = FontFamily.Monospace
        )

        if (protein.related.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(Res.string.related_proteins),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )
            protein.related.forEach { related ->
                TextButton(onClick = { onProteinClick(related.accession) }) {
                    Text(related.accession + (related.geneName?.let { " · $it" } ?: ""))
                }
            }
        }
    }
}
