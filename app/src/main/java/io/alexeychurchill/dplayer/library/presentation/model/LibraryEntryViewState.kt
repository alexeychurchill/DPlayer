package io.alexeychurchill.dplayer.library.presentation.model

data class LibraryEntryViewState(
    val directory: DirectoryEntryViewState,
    val actions: LibraryDirectoryActionsViewState? = null,
)
