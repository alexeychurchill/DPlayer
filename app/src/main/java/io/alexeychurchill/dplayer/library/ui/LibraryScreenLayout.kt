@file:OptIn(ExperimentalMaterial3Api::class)

package io.alexeychurchill.dplayer.library.ui

import android.content.res.Configuration
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import io.alexeychurchill.dplayer.library.presentation.LibraryViewState
import io.alexeychurchill.dplayer.library.presentation.OnLibraryAction
import io.alexeychurchill.dplayer.ui.theme.ClownTheme

@Composable
fun LibraryScreenLayout(
    title: String?,
    state: LibraryViewState,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable (() -> Unit) = {},
    actions: @Composable (RowScope.() -> Unit) = {},
    onLibraryAction: OnLibraryAction = {},
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            LibraryTopBar(
                title = title,
                navigationIcon = navigationIcon,
                actions = actions,
            )
        }
    ) { screenPaddings ->
        Crossfade(
            targetState = state,
            label = "content crossfade",
        ) { currentState ->
            when (currentState) {
                LibraryViewState.Loading -> LoadingLibraryLayout(
                    modifier = Modifier.padding(screenPaddings)
                )

                is LibraryViewState.Loaded -> LoadedLibraryLayout(
                    state = currentState,
                    modifier = Modifier.padding(screenPaddings),
                    onAction = onLibraryAction,
                )
            }
        }
    }
}

@Composable
private fun LibraryTopBar(
    title: String?,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable (() -> Unit) = {},
    actions: @Composable (RowScope.() -> Unit) = {},
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Crossfade(
                targetState = title,
                animationSpec = tween(),
                label = "screen title crossfade",
            ) { titleValue ->
                if (titleValue != null) {
                    Text(text = titleValue)
                } else {
                    AppBarTitlePlaceholder()
                }
            }
        },
        navigationIcon = navigationIcon,
        actions = actions,
    )
}

@Composable
private fun AppBarTitlePlaceholder(modifier: Modifier = Modifier) {
    val placeholderHeight = with(LocalDensity.current) {
        LocalTextStyle.current.copy().lineHeight.toDp()
    }

    BoxWithConstraints(modifier = modifier) {
        Box(
            modifier = Modifier
                .width(maxWidth * 0.7f)
                .height(placeholderHeight)
                .background(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = RoundedCornerShape(4.dp),
                ),
        )
    }
}

@Composable
private fun LoadingLibraryLayout(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize()) {
        repeat(30) { // Let's assume that this is enough to fill the screen
            ShadeLibraryItem(modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
private fun LoadedLibraryLayout(
    state: LibraryViewState.Loaded,
    modifier: Modifier = Modifier,
    onAction: OnLibraryAction = {},
) {
    LibrarySectionList(
        sections = state.sections,
        modifier = modifier,
        onAction = onAction,
    )
}

@Preview(
    widthDp = 360,
    heightDp = 640,
    name = "light",
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    widthDp = 360,
    heightDp = 640,
    name = "dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun LibraryScreenPreview(
    @PreviewParameter(LibraryPreview::class)
    state: LibraryViewState
) {
    ClownTheme {
        LibraryScreenLayout(
            title = "Lib Layout",
            state = state,
            modifier = Modifier.fillMaxSize(),
        ) { }
    }
}

private class LibraryPreview : PreviewParameterProvider<LibraryViewState> {
    override val values: Sequence<LibraryViewState>
        get() {
            return sequence {
                yield(LibraryViewState.Loading)
                yield(LibraryViewState.Loaded(sections = emptyList()))
            }
        }
}
