package io.alexeychurchill.dplayer.library.presentation.mapper

import io.alexeychurchill.dplayer.core.domain.filesystem.FileName.Companion.DefaultUnknownValue
import io.alexeychurchill.dplayer.core.domain.filesystem.FileName.Name
import io.alexeychurchill.dplayer.core.domain.filesystem.FileSystemEntry
import io.alexeychurchill.dplayer.library.domain.MediaEntry
import io.alexeychurchill.dplayer.library.presentation.LibraryAction.OpenMediaEntry
import io.alexeychurchill.dplayer.library.presentation.viewstate.FileEntryViewState
import io.alexeychurchill.dplayer.media.domain.FileMetadata
import io.alexeychurchill.dplayer.media.presentation.CoverArtPath.LocalUri
import javax.inject.Inject

class FileEntryViewStateMapper @Inject constructor() {

    fun mapToViewState(
        entry: MediaEntry,
        metadata: FileMetadata?,
    ): FileEntryViewState {
        val fsEntry = entry.fsEntry as? FileSystemEntry.File
            ?: throw IllegalArgumentException("fsEntry must be a file!")

        return FileEntryViewState(
            path = fsEntry.path,
            visibleTitle = metadata?.title
                ?: (fsEntry.name as? Name)?.value
                ?: DefaultUnknownValue,
            artist = metadata?.artist,
            year = metadata?.year,
            fileExtension = fsEntry.extension,
            coverArtPath = LocalUri(fsEntry.path),
            openAction = OpenMediaEntry.File(fsEntry.path),
        )
    }
}
