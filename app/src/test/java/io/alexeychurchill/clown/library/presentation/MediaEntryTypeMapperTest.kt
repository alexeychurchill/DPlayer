package io.alexeychurchill.clown.library.presentation

import io.alexeychurchill.clown.core.domain.filesystem.FileName
import io.alexeychurchill.clown.core.domain.filesystem.FileSystemEntry
import io.alexeychurchill.clown.library.domain.DirectorySource
import io.alexeychurchill.clown.library.domain.MediaEntry
import io.alexeychurchill.clown.library.presentation.MediaEntryItemViewState.Type
import io.alexeychurchill.clown.library.presentation.MediaEntryTypeMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory

class MediaEntryTypeMapperTest {

    private val mapper = MediaEntryTypeMapper()

    @TestFactory
    fun `media entry to types`(): Collection<DynamicTest> {
        val entryToType = listOf(

            MediaEntry.Directory(
                directoryEntry = null,
                subDirectoryCount = 0,
                musicFileCount = 0,
                source = DirectorySource.FromFileSystem,
            ) to Type.Directory,

            MediaEntry.File(
                fileEntry = FileSystemEntry.File(path = "", name = FileName.Unknown),
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
