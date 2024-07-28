package io.alexeychurchill.dplayer.library.presentation

import io.alexeychurchill.dplayer.core.domain.filesystem.FileName
import io.alexeychurchill.dplayer.core.domain.filesystem.FileSystemEntry
import io.alexeychurchill.dplayer.library.domain.DirectorySource
import io.alexeychurchill.dplayer.library.domain.MediaEntry
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class EntryFileExtensionMapperTest {

    private val mapper = EntryFileExtensionMapper()

    @Test
    fun `file has extension`() {
        val actual = mapper.mapToExtension(
            entry = MediaEntry.File(
                fileEntry = FileSystemEntry.File(
                    path = "",
                    name = FileName.of("test.mp3"),
                    extension = "mp3",
                ),
            )
        )

        assertThat(actual).isEqualTo("mp3")
    }

    @Test
    fun `file has no extension`() {
        val actual = mapper.mapToExtension(
            entry = MediaEntry.File(
                fileEntry = FileSystemEntry.File(
                    path = "",
                    name = FileName.of("test"),
                    extension = null,
                ),
            )
        )

        assertThat(actual).isNull()
    }

    @Test
    fun `trying to use on directory`() {
        val actual = mapper.mapToExtension(
            entry = MediaEntry.Directory(
                directoryEntry = FileSystemEntry.Directory(
                    path = "",
                    name = FileName.of("test.mp3"),
                    exists = true,
                ),
                musicFileCount = 1,
                subDirectoryCount = 1,
                source = DirectorySource.FromFileSystem,
            )
        )

        assertThat(actual).isNull()
    }
}
