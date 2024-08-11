package io.alexeychurchill.dplayer.library.presentation.model

sealed interface AliasOperationViewState {

    data object None : AliasOperationViewState

    data class Editing(val directoryUri: String) : AliasOperationViewState

    data class Removing(val directoryUri: String) : AliasOperationViewState
}
