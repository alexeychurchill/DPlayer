package io.alexeychurchill.clown.library.domain

import io.alexeychurchill.clown.core.domain.filesystem.FileSystemEntry
import java.time.LocalDateTime

sealed interface MediaEntry {

    data class Directory(
        val directoryEntry: FileSystemEntry.Directory?,
        val subDirectoryCount: Int,
        val musicFileCount: Int,
        val source: DirectorySource,
    ) : MediaEntry

    data class File(
        val fileEntry: FileSystemEntry.File,
    ) : MediaEntry
}

sealed interface DirectorySource {

    data object FromFileSystem : DirectorySource

    data class FromUserLibrary(
        val createdAt: LocalDateTime,
        val updatedAt: LocalDateTime,
        val aliasTitle: String?,
    ) : DirectorySource
}
