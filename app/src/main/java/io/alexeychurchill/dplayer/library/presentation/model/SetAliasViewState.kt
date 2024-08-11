package io.alexeychurchill.dplayer.library.presentation.model

sealed interface SetAliasViewState {

    data object None : SetAliasViewState

    data class Editing(val directoryUri: String) : SetAliasViewState
}