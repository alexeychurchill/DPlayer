@file:OptIn(ExperimentalFoundationApi::class)

package io.alexeychurchill.dplayer.library.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import io.alexeychurchill.dplayer.R
import io.alexeychurchill.dplayer.library.presentation.model.EntryContentSectionsViewState
import io.alexeychurchill.dplayer.library.presentation.model.EntryContentViewState
import io.alexeychurchill.dplayer.library.presentation.model.LibraryAction.GoBack
import io.alexeychurchill.dplayer.library.presentation.model.OnLibraryAction
import io.alexeychurchill.dplayer.library.presentation.viewmodel.EntryContentViewModel
import io.alexeychurchill.dplayer.library.ui.list.DirectoryEntryItem
import io.alexeychurchill.dplayer.library.ui.list.FileEntryItem
import io.alexeychurchill.dplayer.library.ui.widgets.LibraryLoadingPlaceholder
import io.alexeychurchill.dplayer.library.ui.widgets.LibraryTopBar

@Composable
fun EntryContentScreen(
    payload: String,
    modifier: Modifier = Modifier,
    viewModel: EntryContentViewModel = hiltViewModel(
        creationCallback = { factory: EntryContentViewModel.Factory ->
            factory.create(payload)
        },
    ),
    onLibraryAction: OnLibraryAction = {},
) {
    BackHandler {
        onLibraryAction(GoBack)
    }

    val title by viewModel.titleState.collectAsState()
    val content by viewModel.contentState.collectAsState()
    Scaffold(
        modifier = modifier,
        topBar = {
            LibraryTopBar(
                title = title,
                navigationIcon = {
                    BackButton {
                        onLibraryAction(GoBack)
                    }
                },
            )
        },
    ) { screenPaddings ->
        Content(
            modifier = Modifier
                .fillMaxSize()
                .padding(screenPaddings),
            state = content,
            onLibraryAction = onLibraryAction,
        )
    }
}

@Composable
private fun BackButton(
    modifier: Modifier = Modifier,
    onTap: () -> Unit = {},
) {
    IconButton(
        modifier = modifier,
        onClick = onTap,
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.TwoTone.ArrowBack,
            contentDescription = null,
        )
    }
}

@Composable
private fun Content(
    state: EntryContentViewState,
    modifier: Modifier = Modifier,
    onLibraryAction: OnLibraryAction = {},
) {
    Crossfade(
        modifier = modifier,
        targetState = state,
        label = "entry content state",
    ) { currentState ->
        when (currentState) {
            EntryContentViewState.Loading -> {
                LibraryLoadingPlaceholder(modifier = Modifier.fillMaxSize())
            }

            is EntryContentViewState.Loaded -> {
                SectionsList(
                    modifier = Modifier.fillMaxSize(),
                    state = currentState.sectionsState,
                    onLibraryAction = onLibraryAction,
                )
            }
        }
    }
}

@Composable
private fun SectionsList(
    state: EntryContentSectionsViewState,
    modifier: Modifier = Modifier,
    onLibraryAction: OnLibraryAction = {},
) {
    LazyColumn(modifier = modifier) {
        /* Directories section */

        if (state.directorySectionHeaderPresent) {
            stickyHeader {
                DirectorySectionHeader()
            }
        }

        items(
            items = state.directoryEntries,
            key = { it.path },
            contentType = { it::class.simpleName },
        ) { entryItem ->
            DirectoryEntryItem(
                entry = entryItem,
                onTap = {
                    onLibraryAction(entryItem.openAction)
                },
            )
        }

        /* Files section */

        if (state.filesSectionHeaderPresent) {
            stickyHeader {
                FileSectionHeader()
            }
        }

        if (state.noFilesHeaderPresent) {
            item {
                FilesAbsentPlaceholder()
            }
        }

        items(
            items = state.fileEntries,
            key = { it.path },
            contentType = { it::class.simpleName },
        ) { entryItem ->
            FileEntryItem(
                state = entryItem,
                onTap = { onLibraryAction(entryItem.openAction) },
            )
        }
    }
}

@Composable
private fun DirectorySectionHeader(modifier: Modifier = Modifier) {
    SectionHeader(
        modifier = modifier,
        title = stringResource(id = R.string.library_section_directories_header_title),
    )
}

@Composable
private fun FileSectionHeader(modifier: Modifier = Modifier) {
    SectionHeader(
        modifier = modifier,
        title = stringResource(id = R.string.library_section_files_header_title),
    )
}

@Composable
private fun FilesAbsentPlaceholder(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.library_section_files_empty).uppercase(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelLarge.copy(
                letterSpacing = 2.sp,
                fontWeight = FontWeight.Bold,
            ),
        )
    }
}


@Composable
private fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 8.dp, start = 16.dp, end = 16.dp),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
        )
    }
}
