package io.alexeychurchill.dplayer.library.presentation.mapper

import io.alexeychurchill.dplayer.core.domain.filesystem.FileName
import io.alexeychurchill.dplayer.core.domain.filesystem.FileSystemEntry
import io.alexeychurchill.dplayer.library.domain.EntrySource
import io.alexeychurchill.dplayer.library.domain.MediaEntry
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime.now

class LibraryEntryViewStateMapperTest {

    private val directoryMapperMock = mockk<DirectoryEntryViewStateMapper>(relaxed = true)

    private val mapper = LibraryEntryViewStateMapper(
        directoryMapper = directoryMapperMock,
    )

    @Test
    fun `if source not is UserLibrary, call fails`() {
        val entry = MediaEntry(
            fsEntry = FileSystemEntry.Directory(
                path = "", name = FileName.of("dname"), exists = true,
            ),
            source = EntrySource.FileSystem,
        )

        assertThrows<IllegalArgumentException> {
            mapper.mapToViewState(entry)
        }
    }

    @Test
    fun `directory mapper is called`() {
        val entry = MediaEntry(
            fsEntry = FileSystemEntry.Directory(
                path = "", name = FileName.of("dname"), exists = true,
            ),
            source = EntrySource.UserLibrary(
                createdAt = now(), updatedAt = now(), aliasTitle = null,
            ),
        )

        mapper.mapToViewState(entry)

        verify {
            directoryMapperMock.mapToViewState(entry)
        }
    }

    /* @Test
    fun `set alias action available if alias not set`() {
        val entry = MediaEntry(
            fsEntry = FileSystemEntry.Directory(
                path = "", name = FileName.of("dname"), exists = true,
            ),
            source = EntrySource.UserLibrary(
                createdAt = now(), updatedAt = now(), aliasTitle = null,
            ),
        )

        val actual = mapper.mapToViewState(entry)

        // TBD
    } */
}
