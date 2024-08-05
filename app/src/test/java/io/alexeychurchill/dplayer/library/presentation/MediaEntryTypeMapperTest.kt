package io.alexeychurchill.dplayer.library.presentation

import io.alexeychurchill.dplayer.core.domain.filesystem.FileName.Unknown
import io.alexeychurchill.dplayer.core.domain.filesystem.FileSystemEntry
import io.alexeychurchill.dplayer.library.domain.EntryInfo
import io.alexeychurchill.dplayer.library.domain.EntrySource
import io.alexeychurchill.dplayer.library.domain.MediaEntry
import io.alexeychurchill.dplayer.library.presentation.MediaEntryItemViewState.Type
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory

class MediaEntryTypeMapperTest {

    private val mapper = MediaEntryTypeMapper()

    @TestFactory
    fun `media entry to types`(): Collection<DynamicTest> {
        val entryToType = listOf(

            MediaEntry(
                fsEntry = FileSystemEntry.Directory(
                    path = "",
                    name = Unknown,
                    exists = true,
                ),
                source = EntrySource.FileSystem,
                info = EntryInfo.Directory(
                    directoryCount = 0,
                    musicFileCount = 0,
                ),
            ) to Type.Directory,

            MediaEntry(
                fsEntry = FileSystemEntry.File(
                    path = "", name = Unknown, extension = null
                ),
            ) to Type.MusicFile,
        )

        return entryToType.map { (entry, expectedType) ->
            dynamicTest("${entry::class.simpleName} is mapped to $expectedType") {
                val actualType = mapper.mapToType(entry)
                assertThat(actualType).isEqualTo(expectedType)
            }
        }
    }
}
