package io.alexeychurchill.clown.library.viewstate

import io.alexeychurchill.clown.core.ui.viewstate.ViewAction

sealed interface LibraryViewAction : ViewAction {

    data object AddFolder : LibraryViewAction

    /**
     * TODO: Pass directory ID/filepath
     */
    data object OpenFolder : LibraryViewAction
}
