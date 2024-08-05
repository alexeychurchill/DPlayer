package io.alexeychurchill.dplayer.library.presentation

import io.alexeychurchill.dplayer.core.domain.filesystem.FileName
import io.alexeychurchill.dplayer.core.domain.filesystem.FileSystemEntry
import io.alexeychurchill.dplayer.library.domain.EntryInfo
import io.alexeychurchill.dplayer.library.domain.EntrySource
import io.alexeychurchill.dplayer.library.domain.MediaEntry
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class EntryFileExtensionMapperTest {

    private val mapper = EntryFileExtensionMapper()

    @Test
    fun `file has extension`() {
        val actual = mapper.mapToExtension(
            entry = MediaEntry(
                fsEntry = FileSystemEntry.File(
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
            entry = MediaEntry(
                fsEntry = FileSystemEntry.File(
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
            entry = MediaEntry(
                fsEntry = FileSystemEntry.Directory(
                    path = "",
                    name = FileName.of("test.mp3"),
                    exists = true,
                ),
                source = EntrySource.FileSystem,
                info = EntryInfo.Directory(
                    musicFileCount = 1,
                    directoryCount = 1,
                ),
            )
        )

        assertThat(actual).isNull()
    }
}
