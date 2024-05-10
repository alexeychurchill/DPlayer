@file:OptIn(ExperimentalMaterial3Api::class)

package io.alexeychurchill.clown.library.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.hilt.navigation.compose.hiltViewModel
import io.alexeychurchill.clown.R
import io.alexeychurchill.clown.core.ui.viewstate.Noop
import io.alexeychurchill.clown.core.ui.viewstate.ViewActionHandler
import io.alexeychurchill.clown.library.presentation.LibraryViewModel
import io.alexeychurchill.clown.library.viewstate.DirectoryStatusViewState
import io.alexeychurchill.clown.library.viewstate.DirectoryViewState
import io.alexeychurchill.clown.library.viewstate.LibraryViewAction
import io.alexeychurchill.clown.library.viewstate.LibraryViewState
import io.alexeychurchill.clown.ui.theme.ClownTheme

@Composable
fun LibraryScreen(
    modifier: Modifier = Modifier,
    viewModel: LibraryViewModel = hiltViewModel()
) {
    val state by viewModel.libraryViewState.collectAsState()
    LibraryScreenLayout(
        modifier = modifier.fillMaxSize(),
        state = state
    )
}

@Composable
private fun LibraryScreenLayout(
    @PreviewParameter(LibraryPreview::class)
    state: LibraryViewState,
    modifier: Modifier = Modifier,
    onEvent: ViewActionHandler = { }
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            LibraryTopBar(onEvent = onEvent)
        }
    ) { screenPaddings ->
        when (state) {
            LibraryViewState.Loading -> LoadingLibraryLayout(
                modifier = Modifier.padding(screenPaddings)
            )

            is LibraryViewState.Loaded -> LoadedLibraryLayout(
                modifier = Modifier.padding(screenPaddings),
                state = state,
                onEvent = onEvent
            )
        }
    }
}

@Composable
private fun LibraryTopBar(
    modifier: Modifier = Modifier,
    onEvent: ViewActionHandler = { }
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(text = stringResource(R.string.library_title))
        },
        actions = {
            IconButton(onClick = { onEvent(LibraryViewAction.AddFolder) }) {
                Icon(
                    imageVector = Icons.TwoTone.Add,
                    contentDescription = null
                )
            }
        }
    )
}

@Composable
private fun LoadingLibraryLayout(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize()) {
        repeat(30) { // Let's assume that this is enough to fill the screen
            LoadingDirectoryListItem(modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
private fun LoadedLibraryLayout(
    state: LibraryViewState.Loaded,
    modifier: Modifier = Modifier,
    onEvent: ViewActionHandler = { }
) {
    LazyColumn(modifier = modifier) {
        items(state.items) { item ->
            DirectoryListItem(
                modifier = Modifier.fillMaxWidth(),
                title = item.title,
                path = item.path,
                status = item.status,
                onClick = { onEvent(item.onPickAction) }
            )
        }
    }
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
            modifier = Modifier.fillMaxSize(),
            state = state
        )
    }
}

private class LibraryPreview : PreviewParameterProvider<LibraryViewState> {
    override val values: Sequence<LibraryViewState>
        get() = sequenceOf(
            LibraryViewState.Loading,
            LibraryViewState.Loaded(
                items = listOf(
                    DirectoryViewState(
                        title = "Music A",
                        path = null,
                        status = DirectoryStatusViewState.NONE,
                        onPickAction = Noop
                    ),
                    DirectoryViewState(
                        title = "Music B",
                        path = "/some/path",
                        status = DirectoryStatusViewState.AVAILABLE,
                        onPickAction = Noop
                    ),
                    DirectoryViewState(
                        title = "Music C",
                        path = "/some/path",
                        status = DirectoryStatusViewState.WARNING,
                        onPickAction = Noop
                    ),
                    DirectoryViewState(
                        title = "Music D",
                        path = null,
                        status = DirectoryStatusViewState.AVAILABLE,
                        onPickAction = Noop
                    ),
                    DirectoryViewState(
                        title = "Music E",
                        path = null,
                        status = DirectoryStatusViewState.WARNING,
                        onPickAction = Noop
                    ),
                )
            )
        )
}
