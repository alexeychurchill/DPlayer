package io.alexeychurchill.clown.library.viewstate

sealed interface LibraryViewState {

    data object Loading : LibraryViewState

    data class Loaded(val items: List<DirectoryViewState>) : LibraryViewState
}
