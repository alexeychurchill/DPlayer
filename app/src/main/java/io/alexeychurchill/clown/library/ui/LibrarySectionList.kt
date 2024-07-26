package io.alexeychurchill.clown.library.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.alexeychurchill.clown.library.presentation.LibrarySectionViewState
import io.alexeychurchill.clown.library.presentation.MediaEntryItemViewState

@Composable
fun LibrarySectionList(
    sections: List<LibrarySectionViewState>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier) {
        for (section in sections) {
            when (section) {
                is LibrarySectionViewState.MediaEntries -> {
                    mediaEntriesSection(section.items)
                }

                else -> { /** NO OP **/ }
            }
        }
    }
}

private fun LazyListScope.mediaEntriesSection(
    items: List<MediaEntryItemViewState>,
) {
    items(
        items = items,
        contentType = MediaEntryItemViewState::type,
    ) { mediaEntry ->
        MediaEntryListItem(entry = mediaEntry)
    }
}
