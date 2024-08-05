package io.alexeychurchill.dplayer.library.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.alexeychurchill.dplayer.R
import io.alexeychurchill.dplayer.library.presentation.LibrarySectionViewState
import io.alexeychurchill.dplayer.library.presentation.LibrarySectionViewState.FilesAbsent
import io.alexeychurchill.dplayer.library.presentation.LibrarySectionViewState.Header
import io.alexeychurchill.dplayer.library.presentation.LibrarySectionViewState.MediaEntries
import io.alexeychurchill.dplayer.library.presentation.MediaEntryItemViewState
import io.alexeychurchill.dplayer.library.presentation.OnLibraryAction

@Composable
fun LibrarySectionList(
    sections: List<LibrarySectionViewState>,
    modifier: Modifier = Modifier,
    onAction: OnLibraryAction = {},
) {
    LazyColumn(modifier = modifier) {
        for (section in sections) {
            when (section) {
                Header.ForDirectories -> {
                    directoriesHeader()
                }

                Header.ForFiles -> {
                    filesHeader()
                }

                is MediaEntries -> {
                    mediaEntriesSection(section.items, onAction)
                }

                FilesAbsent -> {
                    noFilesSection()
                }

                else -> {
                    /** NO OP **/
                }
            }
        }
    }
}

private fun LazyListScope.filesHeader() {
    item {
        Header(
            title = stringResource(id = R.string.library_section_files_header_title),
        )
    }
}

private fun LazyListScope.directoriesHeader() {
    item {
        Header(
            title = stringResource(id = R.string.library_section_directories_header_title),
        )
    }
}

private fun LazyListScope.mediaEntriesSection(
    items: List<MediaEntryItemViewState>,
    onAction: OnLibraryAction,
) {
    items(
        items = items,
        key = MediaEntryItemViewState::path,
        contentType = { it.type.name },
    ) { mediaEntry ->
        MediaEntryListItem(
            entry = mediaEntry,
            onTap = { onAction(mediaEntry.openAction) },
        )
    }
}

private fun LazyListScope.noFilesSection() {
    item {
        NoFiles()
    }
}

@Composable
private fun Header(
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

@Composable
private fun NoFiles(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.library_section_files_empty).uppercase(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelLarge.copy(
                letterSpacing = 2.sp,
                fontWeight = FontWeight.Bold,
            ),
        )
    }
}
