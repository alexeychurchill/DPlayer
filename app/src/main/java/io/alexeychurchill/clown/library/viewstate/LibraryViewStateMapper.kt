package io.alexeychurchill.clown.library.viewstate

import io.alexeychurchill.clown.library.domain.Directory
import javax.inject.Inject

class LibraryViewStateMapper @Inject constructor() {

    fun mapToViewState(items: List<Directory>): LibraryViewState {
        return LibraryViewState.Loaded(
            items = items.map(::mapDirectoryToViewState)
        )
    }

    private fun mapDirectoryToViewState(directory: Directory): DirectoryViewState {
        return DirectoryViewState(
            title = directory.path,
            path = directory.path,
            status = DirectoryStatusViewState.NONE,
            onPickAction = LibraryViewAction.OpenFolder(directory.path)
        )
    }
}
