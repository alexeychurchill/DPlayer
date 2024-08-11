@file:OptIn(ExperimentalMaterial3Api::class)

package io.alexeychurchill.dplayer.library.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import io.alexeychurchill.dplayer.library.ui.list.PlaceholderEntryItem

@Composable
fun LibraryTopBar(
    title: String?,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable (() -> Unit) = {},
    actions: @Composable (RowScope.() -> Unit) = {},
) {
    TopAppBar(
        modifier = modifier,
        title = {
            val titleAlpha by animateFloatAsState(
                targetValue = if (title == null) 0.0f else 1.0f,
                label = "screen title fade",
            )
            Text(
                modifier = Modifier
                    .graphicsLayer { alpha = titleAlpha },
                text = title ?: "",
            )
        },
        navigationIcon = navigationIcon,
        actions = actions,
    )
}

@Composable
fun LibraryLoadingPlaceholder(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize()) {
        repeat(30) { // Let's assume that this is enough to fill the screen
            PlaceholderEntryItem(modifier = Modifier.fillMaxWidth())
        }
    }
}
