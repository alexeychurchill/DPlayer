package io.alexeychurchill.dplayer.library.presentation.mapper

import io.alexeychurchill.dplayer.library.domain.EntrySource
import io.alexeychurchill.dplayer.library.domain.MediaEntry
import io.alexeychurchill.dplayer.library.presentation.model.LibraryDirectoryActionsViewState
import io.alexeychurchill.dplayer.library.presentation.model.LibraryEntryViewState
import javax.inject.Inject

class LibraryEntryViewStateMapper @Inject constructor(
    private val directoryMapper: DirectoryEntryViewStateMapper,
) {

    fun mapToViewState(entry: MediaEntry): LibraryEntryViewState {
        val source = entry.source as? EntrySource.UserLibrary
            ?: throw IllegalArgumentException(
                "Cannot map entries with source other than UserLibrary!"
            )

        val directoryViewState = directoryMapper.mapToViewState(entry)
        return LibraryEntryViewState(
            directory = directoryViewState,
            actions = LibraryDirectoryActionsViewState(
                directoryUri = entry.fsEntry.path,
                directoryTitle = directoryViewState.visibleTitle,
                setAliasEnabled = source.aliasTitle == null,
                updateAliasEnabled = source.aliasTitle != null,
                removeAliasEnabled = source.aliasTitle != null,
            ),
        )
    }
}
