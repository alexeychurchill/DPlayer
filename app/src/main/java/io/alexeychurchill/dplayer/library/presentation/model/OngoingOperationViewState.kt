package io.alexeychurchill.dplayer.library.presentation.model

sealed interface OngoingOperationViewState {

    data object None : OngoingOperationViewState

    data class EditingAlias(
        val directoryUri: String,
    ) : OngoingOperationViewState

    data class RemovingAlias(
        val directoryUri: String,
        val directoryTitle: String,
    ) : OngoingOperationViewState

    data class RemovingFromLibrary(
        val directoryUri: String,
        val directoryTitle: String,
    ) : OngoingOperationViewState
}
