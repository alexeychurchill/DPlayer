package io.alexeychurchill.dplayer.library.presentation.viewstate

import io.alexeychurchill.dplayer.library.presentation.LibraryAction

data class DirectoryEntryViewState(
    val path: String,
    val visibleTitle: String,
    val status: DirectoryStatusViewState,
    val fileCount: Int?,
    val directoryCount: Int?,
    val openAction: LibraryAction,
)

enum class DirectoryStatusViewState {
    None,
    Openable,
    Faulty,
}
