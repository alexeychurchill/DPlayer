package io.alexeychurchill.clown.library.presentation

typealias OnLibraryAction = (LibraryAction) -> Unit

sealed interface LibraryAction {

    data object OpenTreePicker : LibraryAction

    data class TreePicked(val uriPath: String?) : LibraryAction

    data class OpenMediaEntry(
        val type: MediaEntryItemViewState.Type,
        val path: String?,
    ) : LibraryAction
}
