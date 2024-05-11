package io.alexeychurchill.clown.library.domain

import java.time.LocalDateTime

/**
 * Added Directory information
 */
data class Directory(
    val path: String,
    val aliasTitle: String?,
    val addedAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)
