package io.alexeychurchill.dplayer.library.presentation.mapper

import io.alexeychurchill.dplayer.core.domain.filesystem.FileName.Companion.DefaultUnknownValue
import io.alexeychurchill.dplayer.core.domain.filesystem.FileName.Name
import io.alexeychurchill.dplayer.core.domain.filesystem.FileSystemEntry
import io.alexeychurchill.dplayer.library.domain.EntryInfo.Directory
import io.alexeychurchill.dplayer.library.domain.EntrySource.UserLibrary
import io.alexeychurchill.dplayer.library.domain.MediaEntry
import io.alexeychurchill.dplayer.library.presentation.LibraryAction.OpenMediaEntry
import io.alexeychurchill.dplayer.library.presentation.OpenDirectoryPayload
import io.alexeychurchill.dplayer.library.presentation.viewstate.DirectoryEntryViewState
import io.alexeychurchill.dplayer.library.presentation.viewstate.DirectoryStatusViewState
import io.alexeychurchill.dplayer.library.presentation.viewstate.DirectoryStatusViewState.Faulty
import io.alexeychurchill.dplayer.library.presentation.viewstate.DirectoryStatusViewState.None
import io.alexeychurchill.dplayer.library.presentation.viewstate.DirectoryStatusViewState.Openable
import javax.inject.Inject

class DirectoryEntryViewStateMapper @Inject constructor(
    private val statusMapper: DirectoryStatusViewStateMapper,
) {

    fun mapToViewState(entry: MediaEntry): DirectoryEntryViewState {
        val fsEntry = entry.fsEntry as? FileSystemEntry.Directory
            ?: throw IllegalArgumentException("fsEntry must be Directory!")

        val title = (entry.source as? UserLibrary)?.aliasTitle
            ?: (fsEntry.name as? Name)?.value
            ?: DefaultUnknownValue

        val info = entry.info as? Directory
        return DirectoryEntryViewState(
            path = fsEntry.path,
            visibleTitle = title,
            status = statusMapper.mapToViewState(entry),
            fileCount = info?.musicFileCount,
            directoryCount = info?.directoryCount,
            openAction = OpenMediaEntry.Directory(
                payload = OpenDirectoryPayload(
                    title = title,
                    path = fsEntry.path,
                ),
            ),
        )
    }
}

class DirectoryStatusViewStateMapper @Inject internal constructor() {

    fun mapToViewState(entry: MediaEntry): DirectoryStatusViewState {
        val fsEntry = entry.fsEntry
        if (fsEntry !is FileSystemEntry.Directory) {
            return None
        }

        val directoryExists = fsEntry.exists
        val directoryHasChildren = (entry.info as? Directory)
            ?.let { (it.directoryCount + it.musicFileCount) > 0 }
            ?: false

        return when {
            !directoryExists -> Faulty
            directoryExists && directoryHasChildren -> Openable
            else -> None
        }
    }
}
