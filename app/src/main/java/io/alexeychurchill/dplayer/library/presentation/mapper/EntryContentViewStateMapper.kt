package io.alexeychurchill.dplayer.library.presentation.mapper

import io.alexeychurchill.dplayer.core.domain.filesystem.FileSystemEntry
import io.alexeychurchill.dplayer.library.domain.MediaEntry
import io.alexeychurchill.dplayer.library.presentation.model.EntryContentSectionsViewState
import io.alexeychurchill.dplayer.media.domain.FileMetadata
import javax.inject.Inject

class EntryContentViewStateMapper @Inject constructor(
    private val directoryMapper: DirectoryEntryViewStateMapper,
    private val fileMapper: FileEntryViewStateMapper,
) {

    fun mapToViewState(
        items: List<MediaEntry>,
        metadata: Map<String, FileMetadata> = emptyMap(),
    ): EntryContentSectionsViewState {
        val directoryEntries = items
            .filter { entry -> entry.fsEntry is FileSystemEntry.Directory }
            .map(directoryMapper::mapToViewState)

        val fileEntries = items
            .filter { entry -> entry.fsEntry is FileSystemEntry.File }
            .map { entry ->
                fileMapper.mapToViewState(entry, metadata = metadata[entry.fsEntry.path])
            }

        return EntryContentSectionsViewState(
            directorySectionHeaderPresent = directoryEntries.isNotEmpty(),
            directoryEntries = directoryEntries,
            filesSectionHeaderPresent = true,
            fileEntries = fileEntries,
            noFilesHeaderPresent = fileEntries.isEmpty(),
        )
    }
}
