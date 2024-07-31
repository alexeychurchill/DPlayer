package io.alexeychurchill.dplayer.library.domain

import io.alexeychurchill.dplayer.core.domain.filesystem.FileSystemEntry
import java.time.LocalDateTime

data class MediaEntry(
    val fsEntry: FileSystemEntry,
    val source: EntrySource = EntrySource.FileSystem,
    val info: EntryInfo = EntryInfo.None,
)

sealed interface EntrySource {

    data object FileSystem : EntrySource

    data class UserLibrary(
        val createdAt: LocalDateTime,
        val updatedAt: LocalDateTime,
        val aliasTitle: String?,
    ) : EntrySource
}

sealed interface EntryInfo {

    data object None : EntryInfo

    data class Directory(
        val directoryCount: Int,
        val musicFileCount: Int,
    ) : EntryInfo
}
