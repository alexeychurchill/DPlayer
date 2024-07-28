package io.alexeychurchill.dplayer.library.domain

import java.time.LocalDateTime

data class LibraryRecord(
    val dirPath: String,
    val aliasTitle: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)