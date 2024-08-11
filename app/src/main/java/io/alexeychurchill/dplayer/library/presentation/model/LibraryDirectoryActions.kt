package io.alexeychurchill.dplayer.library.presentation.model

typealias OnLibraryDirectoryAction = (LibraryDirectoryAction) -> Unit

data class LibraryDirectoryActionsViewState(
    val setAliasEnabled: Boolean = false,
    val updateAliasEnabled: Boolean = false,
    val removeAliasEnabled: Boolean = false,
)

sealed class LibraryDirectoryAction(open val path: String) {

    data class SetAlias(
        override val path: String,
    ) : LibraryDirectoryAction(path)

    data class UpdateAlias(
        override val path: String,
    ) : LibraryDirectoryAction(path)

    data class RemoveAlias(
        override val path: String,
    ) : LibraryDirectoryAction(path)
}
