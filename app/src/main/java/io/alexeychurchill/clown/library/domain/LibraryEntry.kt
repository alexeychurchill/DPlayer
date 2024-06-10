package io.alexeychurchill.clown.library.domain

import io.alexeychurchill.clown.core.domain.filesystem.FileSystemEntry
import java.time.LocalDateTime

data class LibraryEntry(
    val directory: FileSystemEntry.Directory?,
    val aliasTitle: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)
