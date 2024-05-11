package io.alexeychurchill.clown.library.viewstate

import io.alexeychurchill.clown.core.viewstate.ViewAction

sealed interface LibraryViewAction : ViewAction {

    data object AddFolder : LibraryViewAction

    data class OnFolderPicked(val treeUri: String?) : LibraryViewAction

    data class OpenFolder(val treeUri: String) : LibraryViewAction
}
