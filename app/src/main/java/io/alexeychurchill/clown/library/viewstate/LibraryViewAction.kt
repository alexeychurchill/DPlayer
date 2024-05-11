package io.alexeychurchill.clown.library.viewstate

import io.alexeychurchill.clown.core.viewstate.ViewAction

sealed interface LibraryViewAction : ViewAction {

    data object AddFolder : LibraryViewAction

    data class OnFolderPicked(val treeUri: String?) : LibraryViewAction

    /**
     * TODO: Pass directory ID/filepath
     */
    data object OpenFolder : LibraryViewAction
}
