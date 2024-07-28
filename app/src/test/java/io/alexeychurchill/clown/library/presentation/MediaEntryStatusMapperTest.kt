package io.alexeychurchill.clown.library.presentation

import io.alexeychurchill.clown.core.domain.filesystem.FileName
import io.alexeychurchill.clown.core.domain.filesystem.FileSystemEntry
import io.alexeychurchill.clown.library.domain.DirectorySource
import io.alexeychurchill.clown.library.domain.MediaEntry
import io.alexeychurchill.clown.library.presentation.MediaEntryItemViewState
import io.alexeychurchill.clown.library.presentation.MediaEntryStatusMapper
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class MediaEntryStatusMapperTest {

    private val mapper = MediaEntryStatusMapper()

    @Test
    fun `status for file`() {
        val mediaEntry = MediaEntry.File(
            fileEntry = FileSystemEntry.File(path = "", name = FileName.Unknown),
        )

        val actual = mapper.mapToStatus(mediaEntry)
        Assertions.assertThat(actual).isEqualTo(MediaEntryItemViewState.Status.None)
    }

    @Test
    fun `status for non existent directory`() {
        val mediaEntry = MediaEntry.Directory(
            directoryEntry = FileSystemEntry.Directory(
                path = "",
                name = FileName.Unknown,
                exists = false,
            ),
            musicFileCount = 1,
            subDirectoryCount = 1,
            source = DirectorySource.FromFileSystem,
        )

        val actual = mapper.mapToStatus(mediaEntry)
        Assertions.assertThat(actual).isEqualTo(MediaEntryItemViewState.Status.Faulty)
    }

    @Test
    fun `status for null filesystem entry directory`() {
        val mediaEntry = MediaEntry.Directory(
            directoryEntry = null,
            musicFileCount = 0,
            subDirectoryCount = 0,
            source = DirectorySource.FromFileSystem,
        )

        val actual = mapper.mapToStatus(mediaEntry)
        Assertions.assertThat(actual).isEqualTo(MediaEntryItemViewState.Status.Faulty)
    }

    @Test
    fun `status for empty directory`() {
        val mediaEntry = MediaEntry.Directory(
            directoryEntry = FileSystemEntry.Directory(
                path = "",
                name = FileName.Unknown,
                exists = true,
            ),
            musicFileCount = 0,
            subDirectoryCount = 0,
            source = DirectorySource.FromFileSystem,
        )

        val actual = mapper.mapToStatus(mediaEntry)
        Assertions.assertThat(actual).isEqualTo(MediaEntryItemViewState.Status.None)
    }

    @Test
    fun `status for directory with directories`() {
        val mediaEntry = MediaEntry.Directory(
            directoryEntry = FileSystemEntry.Directory(
                path = "",
                name = FileName.Unknown,
                exists = true,
            ),
            musicFileCount = 0,
            subDirectoryCount = 1,
            source = DirectorySource.FromFileSystem,
        )

        val actual = mapper.mapToStatus(mediaEntry)
        Assertions.assertThat(actual).isEqualTo(MediaEntryItemViewState.Status.Openable)
    }

    @Test
    fun `status for directory with files`() {
        val mediaEntry = MediaEntry.Directory(
            directoryEntry = FileSystemEntry.Directory(
                path = "",
                name = FileName.Unknown,
                exists = true,
            ),
            musicFileCount = 1,
            subDirectoryCount = 0,
            source = DirectorySource.FromFileSystem,
        )

        val actual = mapper.mapToStatus(mediaEntry)
        Assertions.assertThat(actual).isEqualTo(MediaEntryItemViewState.Status.Openable)
    }

    @Test
    fun `status for directory with files and directories`() {
        val mediaEntry = MediaEntry.Directory(
            directoryEntry = FileSystemEntry.Directory(
                path = "",
                name = FileName.Unknown,
                exists = true,
            ),
            musicFileCount = 1,
            subDirectoryCount = 1,
            source = DirectorySource.FromFileSystem,
        )

        val actual = mapper.mapToStatus(mediaEntry)
        Assertions.assertThat(actual).isEqualTo(MediaEntryItemViewState.Status.Openable)
    }
}