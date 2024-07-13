package io.alexeychurchill.clown.library.viewstate

import io.alexeychurchill.clown.core.domain.filesystem.FileName
import io.alexeychurchill.clown.core.viewstate.ViewAction
import io.alexeychurchill.clown.library.domain.DirectorySource
import io.alexeychurchill.clown.library.domain.MediaEntry
import javax.inject.Inject

class FileLibraryListItemStateMapper @Inject constructor() {

    fun mapToListItemState(file: MediaEntry.File): LibraryListItemState.Entry {
        return LibraryListItemState.Entry(
            title = (file.fileEntry.name as? FileName.Name)?.value ?: FileName.DefaultUnknownValue,
            type = LibraryListItemState.Entry.Type.MusicFile,
            onTap = ViewAction.todo(),
        )
    }
}

class DirectoryLibraryListItemStateMapper @Inject constructor() {

    fun mapToListItemState(directory: MediaEntry.Directory): LibraryListItemState.Entry {
        val directoryTitle = (directory.source as? DirectorySource.FromUserLibrary)?.aliasTitle
            ?: (directory.directoryEntry?.name as? FileName.Name)?.value
            ?: FileName.DefaultUnknownValue

        val exists = directory.directoryEntry?.exists == true
        val hasChildren = (directory.subDirectoryCount + directory.musicFileCount) > 0
        return LibraryListItemState.Entry(
            title = directoryTitle,
            type = LibraryListItemState.Entry.Type.Directory,
            status = when {
                !exists -> LibraryListItemState.Entry.Status.Faulty

                exists && hasChildren -> LibraryListItemState.Entry.Status.Openable

                else -> LibraryListItemState.Entry.Status.None
            },
            metaItems = buildList {
                add(LibraryListItemMeta.DirectoryCount(directory.subDirectoryCount))
                add(LibraryListItemMeta.MusicFileCount(directory.musicFileCount))
            },
            onTap = ViewAction.todo(),
        )
    }
}
