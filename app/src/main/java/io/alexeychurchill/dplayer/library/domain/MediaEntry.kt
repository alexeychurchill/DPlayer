package io.alexeychurchill.dplayer.library.domain

import io.alexeychurchill.dplayer.core.domain.filesystem.FileSystemEntry
import io.alexeychurchill.dplayer.library.domain.MediaEntry.Directory
import io.alexeychurchill.dplayer.library.domain.MediaEntry.File
import java.time.LocalDateTime

/**
 * TODO: Remove [Directory] and [File]. This thing kind of violates SSoT rule
 * (and, basic, OOD principles, I believe). So, it's better to have MediaEntry
 * as a simple data class, which "is data-driven", which has a file system entry
 * indicating what kind of media entry this is, which, probably, has additional
 * media entry-related meta info fields, but not file/dir/etc-related fields.
 *
 * TODO: Also, consider redesigning FileSystemEntry either
 */
sealed interface MediaEntry {

    data class Directory(
        val directoryEntry: FileSystemEntry.Directory,
        val subDirectoryCount: Int,
        val musicFileCount: Int,
        val source: EntrySource,
    ) : MediaEntry

    data class File(
        val fileEntry: FileSystemEntry.File,
    ) : MediaEntry
}

sealed interface EntrySource {

    data object FileSystem : EntrySource

    data class UserLibrary(
        val createdAt: LocalDateTime,
        val updatedAt: LocalDateTime,
        val aliasTitle: String?,
    ) : EntrySource
}
