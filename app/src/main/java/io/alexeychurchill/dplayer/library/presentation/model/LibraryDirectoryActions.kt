package io.alexeychurchill.dplayer.library.presentation.model

typealias OnLibraryDirectoryAction = (LibraryDirectoryAction) -> Unit

data class LibraryDirectoryActionsViewState(
    val directoryUri: String,
    val directoryTitle: String,
    val setAliasEnabled: Boolean = false,
    val updateAliasEnabled: Boolean = false,
    val removeAliasEnabled: Boolean = false,
)

sealed class LibraryDirectoryAction(open val directoryUri: String) {

    data class SetAlias(
        override val directoryUri: String,
    ) : LibraryDirectoryAction(directoryUri)

    data class UpdateAlias(
        override val directoryUri: String,
    ) : LibraryDirectoryAction(directoryUri)

    data class RemoveAlias(
        override val directoryUri: String,
        val directoryTitle: String,
    ) : LibraryDirectoryAction(directoryUri)
}
