package io.alexeychurchill.clown.library.domain

import io.alexeychurchill.clown.core.domain.filesystem.FileSystemEntry
import java.time.LocalDateTime

sealed class LibraryEntry(open val fileSystemEntry: FileSystemEntry?) {

    data class Directory(
        val directoryEntry: FileSystemEntry.Directory?,
        val subDirectoryCount: Int,
        val musicFileCount: Int,
        val source: DirectorySource,
    ) : LibraryEntry(directoryEntry) {

        companion object {
            const val DefaultName = "-"
        }
    }

    data class File(
        val fileEntry: FileSystemEntry.File,
    ) : LibraryEntry(fileEntry)
}

sealed interface DirectorySource {

    data object FromFileSystem : DirectorySource

    data class FromUserLibrary(
        val createdAt: LocalDateTime,
        val updatedAt: LocalDateTime,
        val aliasTitle: String?,
    ) : DirectorySource
}
