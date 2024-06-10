package io.alexeychurchill.clown.library.viewstate

import io.alexeychurchill.clown.core.viewstate.ViewAction
import io.alexeychurchill.clown.library.domain.LibraryEntry

sealed interface LibraryViewAction : ViewAction {

    data object AddFolder : LibraryViewAction

    data class OnFolderPicked(val treeUri: String?) : LibraryViewAction

    data class OpenLibraryEntry(val entry: LibraryEntry) : LibraryViewAction
}
