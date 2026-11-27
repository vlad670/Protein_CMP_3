package org.protein.project.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import org.jetbrains.compose.resources.stringResource
import org.protein.project.resources.Res
import org.protein.project.resources.action_toggle_lang
import org.protein.project.resources.action_toggle_theme
import org.protein.project.resources.app_title
import org.protein.project.ui.currentLanguage
import org.protein.project.ui.isDarkTheme
import org.protein.project.ui.locale.setAppLocale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    content: @Composable () -> Unit
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.app_title),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = navigationIcon,
                actions = {
                    actions()
                    IconButton(onClick = {
                        val next = if (currentLanguage == "ru") "en" else "ru"
                        setAppLocale(next)
                        currentLanguage = next
                    }) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = stringResource(Res.string.action_toggle_lang)
                        )
                    }
                    IconButton(onClick = { isDarkTheme = !isDarkTheme }) {
                        Icon(
                            imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = stringResource(Res.string.action_toggle_theme)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { insets ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(insets)
        ) {
            content()
        }
    }
}
