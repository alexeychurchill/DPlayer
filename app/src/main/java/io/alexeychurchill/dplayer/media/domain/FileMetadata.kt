package io.alexeychurchill.dplayer.media.domain

data class FileMetadata(
    val title: String? = null,
    val artist: String? = null,
    val album: String? = null,
    val genre: String? = null,
    val year: Int? = null,
)
