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
