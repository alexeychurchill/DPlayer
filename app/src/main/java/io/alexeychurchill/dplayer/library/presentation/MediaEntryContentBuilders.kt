package io.alexeychurchill.dplayer.library.presentation

import io.alexeychurchill.dplayer.core.domain.filesystem.FileSystemEntry
import io.alexeychurchill.dplayer.library.domain.MediaEntry
import io.alexeychurchill.dplayer.library.presentation.LibrarySectionViewState.FilesAbsent
import io.alexeychurchill.dplayer.library.presentation.LibrarySectionViewState.Header
import io.alexeychurchill.dplayer.library.presentation.LibrarySectionViewState.MediaEntries
import javax.inject.Inject

class AggregateSectionsBuilder @Inject constructor(
    private val directorySectionBuilder: DirectorySectionBuilder,
    private val fileSectionBuilder: FileSectionBuilder,
) {

    fun build(entries: List<MediaEntry>): List<LibrarySectionViewState> {
        val directorySection = directorySectionBuilder.build(entries)
        val fileSectionBuilder = fileSectionBuilder.build(entries)
        return buildList {
            addAll(directorySection)
            addAll(fileSectionBuilder)
        }
    }
}

class DirectorySectionBuilder @Inject constructor(
    private val mediaEntryMapper: MediaEntryViewStateMapper,
) {

    fun build(items: List<MediaEntry>): List<LibrarySectionViewState> {
        val dirEntries = items.filter { it.fsEntry is FileSystemEntry.Directory }
        return buildList {
            if (dirEntries.isNotEmpty()) {
                add(Header.ForDirectories)
                add(MediaEntries(items = dirEntries.map(mediaEntryMapper::mapToViewState)))
            }
        }
    }
}

class FileSectionBuilder @Inject constructor(
    private val mediaEntryMapper: MediaEntryViewStateMapper,
) {

    fun build(items: List<MediaEntry>): List<LibrarySectionViewState> {
        val fileEntries = items.filter { it.fsEntry is FileSystemEntry.File }
        return buildList {
            add(Header.ForFiles)
            if (fileEntries.isNotEmpty()) {
                add(MediaEntries(items = fileEntries.map(mediaEntryMapper::mapToViewState)))
            } else {
                add(FilesAbsent)
            }
        }
    }
}
