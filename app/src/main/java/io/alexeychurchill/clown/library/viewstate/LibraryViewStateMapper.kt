package io.alexeychurchill.clown.library.viewstate

import io.alexeychurchill.clown.core.domain.filesystem.FileName
import io.alexeychurchill.clown.library.domain.LibraryEntry
import javax.inject.Inject

class LibraryViewStateMapper @Inject constructor() {

    private companion object {
        const val DEFAULT_NAME = "-"
    }

    fun mapToViewState(items: List<LibraryEntry>): LibraryViewState {
        return LibraryViewState.Loaded(
            items = items.map(::mapDirectoryToViewState)
        )
    }

    private fun mapDirectoryToViewState(entry: LibraryEntry): DirectoryViewState {
        val dir = entry.directory
        return DirectoryViewState(
            title = mapDirectoryName(entry),
            status = when {
                dir == null -> {
                    DirectoryStatusViewState.NONE
                }

                dir.exists && (entry.directoryCount + entry.musicFileCount) > 0 -> {
                    DirectoryStatusViewState.AVAILABLE
                }

                !dir.exists -> {
                    DirectoryStatusViewState.WARNING
                }

                else -> {
                    DirectoryStatusViewState.NONE
                }
            },
            onPickAction = LibraryViewAction.OpenLibraryEntry(entry),
            dirCount = entry.directoryCount,
            fileCount = entry.musicFileCount,
        )
    }

    private fun mapDirectoryName(entry: LibraryEntry) =
        entry.aliasTitle ?: (entry.directory?.name as? FileName.Name)?.value ?: DEFAULT_NAME
}
