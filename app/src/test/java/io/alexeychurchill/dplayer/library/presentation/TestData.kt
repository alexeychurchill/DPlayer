package io.alexeychurchill.dplayer.library.presentation

import io.alexeychurchill.dplayer.core.domain.filesystem.FileName
import io.alexeychurchill.dplayer.core.domain.filesystem.FileSystemEntry
import io.alexeychurchill.dplayer.library.domain.EntryInfo
import io.alexeychurchill.dplayer.library.domain.EntrySource
import io.alexeychurchill.dplayer.library.domain.MediaEntry

object TestMediaEntries {

    val directoryMediaEntries = List(3) { index ->
        MediaEntry(
            fsEntry = FileSystemEntry.Directory(
                path = "path/dirEntry$index",
                name = FileName.of(name = "DirName$index"),
                exists = true,
            ),
            source = EntrySource.FileSystem,
            info = EntryInfo.Directory(
                directoryCount = 1,
                musicFileCount = 1,
            )
        )
    }

    val fileMediaEntries = List(5) { index ->
        MediaEntry(
            fsEntry = FileSystemEntry.File(
                path = "path/fileEntry$index",
                name = FileName.of(name = "FileName$index"),
                extension = null,
            ),
        )
    }

    val mixed: List<MediaEntry>
        get() = directoryMediaEntries + fileMediaEntries
}

object TestMediaEntryViewStates {

    val directory = List(3) { index ->
        MediaEntryItemViewState(
            path = "path$index",
            title = "title$index",
            type = MediaEntryItemViewState.Type.Directory,
            status = MediaEntryItemViewState.Status.Openable,
            directoryChildInfo = null,
        )
    }

    val file = List(3) { index ->
        MediaEntryItemViewState(
            path = "path$index",
            title = "title$index",
            type = MediaEntryItemViewState.Type.MusicFile,
        )
    }

    val mixed = directory + file
}
