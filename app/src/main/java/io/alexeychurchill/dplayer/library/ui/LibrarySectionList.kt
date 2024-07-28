package io.alexeychurchill.dplayer.library.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.alexeychurchill.dplayer.library.presentation.LibraryAction
import io.alexeychurchill.dplayer.library.presentation.LibrarySectionViewState
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
                is LibrarySectionViewState.MediaEntries -> {
                    mediaEntriesSection(section.items, onAction)
                }

                else -> { /** NO OP **/ }
            }
        }
    }
}

private fun LazyListScope.mediaEntriesSection(
    items: List<MediaEntryItemViewState>,
    onAction: OnLibraryAction,
) {
    items(
        items = items,
        /* key = { it.path ?: "" /** TODO: Remove this after refactoring MediaEntry **/ }, */
        contentType = MediaEntryItemViewState::type,
    ) { mediaEntry ->
        MediaEntryListItem(
            entry = mediaEntry,
            onTap = {
                val action = LibraryAction.OpenMediaEntry(mediaEntry.type, mediaEntry.path)
                onAction(action)
            },
        )
    }
}
