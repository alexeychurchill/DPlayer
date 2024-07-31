package io.alexeychurchill.dplayer.library.presentation

import io.alexeychurchill.dplayer.core.domain.filesystem.FileName.Unknown
import io.alexeychurchill.dplayer.core.domain.filesystem.FileSystemEntry
import io.alexeychurchill.dplayer.library.domain.EntrySource
import io.alexeychurchill.dplayer.library.domain.MediaEntry
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DirectoryEntryChildInfoMapperTest {

    private val mapper = DirectoryEntryChildInfoMapper()

    @Test
    fun `directory has children info`() {
        val directoryEntry = MediaEntry.Directory(
            directoryEntry = FileSystemEntry.Directory(path = "", name = Unknown, exists = true),
            subDirectoryCount = 1,
            musicFileCount = 1,
            source = EntrySource.FileSystem,
        )

        val actual = mapper.mapToChildInfo(directoryEntry)
        assertThat(actual).isEqualTo(
            DirectoryChildInfoViewState(subDirectoryCount = 1, fileCount = 1)
        )
    }

    @Test
    fun `null is returned for file media entry`() {
        val directoryEntry = MediaEntry.File(
            fileEntry = FileSystemEntry.File(path = "", name = Unknown, extension = null),
        )

        val actual = mapper.mapToChildInfo(directoryEntry)
        assertThat(actual).isNull()
    }
}
