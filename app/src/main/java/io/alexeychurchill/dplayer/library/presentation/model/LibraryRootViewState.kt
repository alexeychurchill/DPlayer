package io.alexeychurchill.dplayer.library.presentation.model

sealed interface LibraryRootViewState {

    data object Loading : LibraryRootViewState

    data class Loaded(val items: List<LibraryEntryViewState>) : LibraryRootViewState
}
