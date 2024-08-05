package io.alexeychurchill.dplayer.library.presentation

import io.alexeychurchill.dplayer.core.domain.filesystem.FileName
import io.alexeychurchill.dplayer.core.domain.filesystem.FileSystemEntry
import io.alexeychurchill.dplayer.library.domain.EntryInfo
import io.alexeychurchill.dplayer.library.domain.EntrySource
import io.alexeychurchill.dplayer.library.domain.MediaEntry
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class MediaEntryStatusMapperTest {

    private val mapper = MediaEntryStatusMapper()

    @Test
    fun `status for file`() {
        val mediaEntry = MediaEntry(
            fsEntry = FileSystemEntry.File(path = "", name = FileName.Unknown, extension = null),
        )

        val actual = mapper.mapToStatus(mediaEntry)
        Assertions.assertThat(actual).isEqualTo(MediaEntryItemViewState.Status.None)
    }

    @Test
    fun `status for non existent directory`() {
        val mediaEntry = MediaEntry(
            fsEntry = FileSystemEntry.Directory(
                path = "",
                name = FileName.Unknown,
                exists = false,
            ),
            source = EntrySource.FileSystem,
            info = EntryInfo.Directory(
                musicFileCount = 1,
                directoryCount = 1,
            ),
        )

        val actual = mapper.mapToStatus(mediaEntry)
        Assertions.assertThat(actual).isEqualTo(MediaEntryItemViewState.Status.Faulty)
    }

    @Test
    fun `status for empty directory`() {
        val mediaEntry = MediaEntry(
            fsEntry = FileSystemEntry.Directory(
                path = "",
                name = FileName.Unknown,
                exists = true,
            ),
            source = EntrySource.FileSystem,
            info = EntryInfo.Directory(
                musicFileCount = 0,
                directoryCount = 0,
            ),
        )

        val actual = mapper.mapToStatus(mediaEntry)
        Assertions.assertThat(actual).isEqualTo(MediaEntryItemViewState.Status.None)
    }

    @Test
    fun `status for directory with directories`() {
        val mediaEntry = MediaEntry(
            fsEntry = FileSystemEntry.Directory(
                path = "",
                name = FileName.Unknown,
                exists = true,
            ),
            source = EntrySource.FileSystem,
            info = EntryInfo.Directory(
                musicFileCount = 0,
                directoryCount = 1,
            )
        )

        val actual = mapper.mapToStatus(mediaEntry)
        Assertions.assertThat(actual).isEqualTo(MediaEntryItemViewState.Status.Openable)
    }

    @Test
    fun `status for directory with files`() {
        val mediaEntry = MediaEntry(
            fsEntry = FileSystemEntry.Directory(
                path = "",
                name = FileName.Unknown,
                exists = true,
            ),
            source = EntrySource.FileSystem,
            info = EntryInfo.Directory(
                musicFileCount = 1,
                directoryCount = 0,
            ),
        )

        val actual = mapper.mapToStatus(mediaEntry)
        Assertions.assertThat(actual).isEqualTo(MediaEntryItemViewState.Status.Openable)
    }

    @Test
    fun `status for directory with files and directories`() {
        val mediaEntry = MediaEntry(
            fsEntry = FileSystemEntry.Directory(
                path = "",
                name = FileName.Unknown,
                exists = true,
            ),
            source = EntrySource.FileSystem,
            info = EntryInfo.Directory(
                musicFileCount = 1,
                directoryCount = 1,
            ),
        )

        val actual = mapper.mapToStatus(mediaEntry)
        Assertions.assertThat(actual).isEqualTo(MediaEntryItemViewState.Status.Openable)
    }
}
