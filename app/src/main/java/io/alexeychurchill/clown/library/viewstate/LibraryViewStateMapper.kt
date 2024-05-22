package io.alexeychurchill.clown.library.viewstate

import io.alexeychurchill.clown.library.domain.Directory
import io.alexeychurchill.clown.library.domain.DirectoryStatus
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
            status = when (directory.status) {
                DirectoryStatus.Available -> DirectoryStatusViewState.AVAILABLE
                DirectoryStatus.Unavailable -> DirectoryStatusViewState.WARNING
                else -> DirectoryStatusViewState.NONE
            },
            onPickAction = LibraryViewAction.OpenFolder(directory.path),
            dirCount = directory.dirCount,
            fileCount = directory.fileCount,
        )
    }

    private fun mapDirectoryName(directory: Directory) =
        directory.aliasTitle ?: (directory.name as? FileName.Name)?.value ?: DEFAULT_NAME
}
