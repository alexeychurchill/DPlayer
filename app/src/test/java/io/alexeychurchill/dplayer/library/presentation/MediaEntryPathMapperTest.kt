package io.alexeychurchill.dplayer.library.presentation

import io.alexeychurchill.dplayer.core.domain.filesystem.FileName
import io.alexeychurchill.dplayer.core.domain.filesystem.FileSystemEntry
import io.alexeychurchill.dplayer.library.domain.EntrySource
import io.alexeychurchill.dplayer.library.domain.MediaEntry
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


class MediaEntryPathMapperTest {

    private val mapper = MediaEntryPathMapper()

    @Test
    fun `path for file media entry`() {
        val path = "/path"
        val mediaEntry = MediaEntry.File(
            fileEntry = FileSystemEntry.File(
                path = path, name = FileName.Unknown, extension = null
            ),
        )

        val actual = mapper.mapToPath(mediaEntry)
        assertThat(actual).isEqualTo(path)
    }

    @Test
    fun `path for dir media entry`() {
        val path = "/path"
        val mediaEntry = MediaEntry.Directory(
            directoryEntry = FileSystemEntry.Directory(
                path = path,
                name = FileName.Unknown,
                exists = true,
            ),
            musicFileCount = 0,
            subDirectoryCount = 0,
            source = EntrySource.FileSystem,
        )

        val actual = mapper.mapToPath(mediaEntry)
        assertThat(actual).isEqualTo(path)
    }
}
