package io.alexeychurchill.clown.library.presentation

import io.alexeychurchill.clown.core.domain.filesystem.FileName
import io.alexeychurchill.clown.core.domain.filesystem.FileSystemEntry
import io.alexeychurchill.clown.library.domain.DirectorySource
import io.alexeychurchill.clown.library.domain.MediaEntry
import io.alexeychurchill.clown.library.presentation.MediaEntryPathMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


class MediaEntryPathMapperTest {

    private val mapper = MediaEntryPathMapper()

    @Test
    fun `path for file media entry`() {
        val path = "/path"
        val mediaEntry = MediaEntry.File(
            fileEntry = FileSystemEntry.File(path = path, name = FileName.Unknown),
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
            source = DirectorySource.FromFileSystem,
        )

        val actual = mapper.mapToPath(mediaEntry)
        assertThat(actual).isEqualTo(path)
    }

    @Test
    fun `path for null dir media entry`() {
        val mediaEntry = MediaEntry.Directory(
            directoryEntry = null,
            musicFileCount = 0,
            subDirectoryCount = 0,
            source = DirectorySource.FromFileSystem,
        )

        val actual = mapper.mapToPath(mediaEntry)
        assertThat(actual).isNull()
    }
}
