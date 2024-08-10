package io.alexeychurchill.dplayer.library.presentation.viewstate

import io.alexeychurchill.dplayer.library.presentation.LibraryAction
import io.alexeychurchill.dplayer.media.presentation.CoverArtPath

data class FileEntryViewState(
    val path: String,
    val visibleTitle: String,
    val artist: String? = null,
    val year: Int? = null,
    val fileExtension: String? = null,
    val coverArtPath: CoverArtPath? = null,
    val openAction: LibraryAction,
)
