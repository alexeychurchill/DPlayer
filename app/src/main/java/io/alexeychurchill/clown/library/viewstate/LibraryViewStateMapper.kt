package io.alexeychurchill.clown.library.viewstate

import io.alexeychurchill.clown.library.domain.Directory
import io.alexeychurchill.clown.library.domain.FileName
import javax.inject.Inject

class LibraryViewStateMapper @Inject constructor() {

    private companion object {
        const val DEFAULT_NAME = "-"
    }

    fun mapToViewState(items: List<Directory>): LibraryViewState {
        return LibraryViewState.Loaded(
            items = items.map(::mapDirectoryToViewState)
        )
    }

    private fun mapDirectoryToViewState(directory: Directory): DirectoryViewState {
        return DirectoryViewState(
            title = mapDirectoryName(directory),
            path = directory.path,
            status = DirectoryStatusViewState.NONE,
            onPickAction = LibraryViewAction.OpenFolder(directory.path)
        )
    }

    private fun mapDirectoryName(directory: Directory) =
        directory.aliasTitle ?: (directory.name as? FileName.Name)?.value ?: DEFAULT_NAME
}
