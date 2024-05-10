package io.alexeychurchill.clown.library.viewstate

import io.alexeychurchill.clown.core.ui.viewstate.ViewAction

data class DirectoryViewState(
    val title: String,
    val path: String?,
    val status: DirectoryStatusViewState,
    val onPickAction: ViewAction
)
