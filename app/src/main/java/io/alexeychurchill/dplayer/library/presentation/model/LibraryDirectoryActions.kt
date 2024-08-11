package io.alexeychurchill.dplayer.library.presentation.model

typealias OnLibraryDirectoryAction = (LibraryDirectoryAction) -> Unit

data class LibraryDirectoryActionsViewState(
    val setAliasEnabled: Boolean = false,
    val removeAliasEnabled: Boolean = false,
)

sealed interface LibraryDirectoryAction {

    data object SetAlias : LibraryDirectoryAction

    data object RemoveAlias : LibraryDirectoryAction
}
