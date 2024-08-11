package io.alexeychurchill.dplayer.library.presentation.mapper

import io.alexeychurchill.dplayer.core.domain.filesystem.FileName
import io.alexeychurchill.dplayer.core.domain.filesystem.FileSystemEntry
import io.alexeychurchill.dplayer.library.domain.EntrySource
import io.alexeychurchill.dplayer.library.domain.MediaEntry
import io.alexeychurchill.dplayer.library.presentation.model.DirectoryEntryViewState
import io.alexeychurchill.dplayer.library.presentation.model.DirectoryStatusViewState
import io.alexeychurchill.dplayer.library.presentation.model.LibraryAction
import io.alexeychurchill.dplayer.library.presentation.model.OpenDirectoryPayload
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime.now

class LibraryEntryViewStateMapperTest {

    private companion object {
        const val VisibleTitle = "visibleTitile"
    }

    private val directoryMapperMock = mockk<DirectoryEntryViewStateMapper>(relaxed = true) {
        every { mapToViewState(any()) } returns DirectoryEntryViewState(
            path = "",
            visibleTitle = VisibleTitle,
            status = DirectoryStatusViewState.None,
            fileCount = 1,
            directoryCount = 2,
            openAction = LibraryAction.OpenMediaEntry.Directory(
                payload = OpenDirectoryPayload(
                    path = "",
                    title = "visibleTitle",
                )
            )
        )
    }

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
    fun `basic mapping is working`() {
        val path = "dpath"
        val entry = MediaEntry(
            fsEntry = FileSystemEntry.Directory(
                path = path, name = FileName.of("dname"), exists = true,
            ),
            source = EntrySource.UserLibrary(
                createdAt = now(), updatedAt = now(), aliasTitle = null,
            ),
        )

        val actual = mapper.mapToViewState(entry)

        verify { directoryMapperMock.mapToViewState(entry) }
        assertThat(actual.actions?.directoryUri).isEqualTo(path)
        assertThat(actual.actions?.directoryTitle).isEqualTo(VisibleTitle)
    }

    @Test
    fun `set alias action is available if alias not set`() {
        val entry = MediaEntry(
            fsEntry = FileSystemEntry.Directory(
                path = "", name = FileName.of("dname"), exists = true,
            ),
            source = EntrySource.UserLibrary(
                createdAt = now(), updatedAt = now(), aliasTitle = null,
            ),
        )

        val actual = mapper.mapToViewState(entry)

        assertThat(actual.actions?.setAliasEnabled).isTrue()
    }

    @Test
    fun `update alias and remove alias actions are available if alias set`() {
        val entry = MediaEntry(
            fsEntry = FileSystemEntry.Directory(
                path = "", name = FileName.of("dname"), exists = true,
            ),
            source = EntrySource.UserLibrary(
                createdAt = now(), updatedAt = now(), aliasTitle = "null",
            ),
        )

        val actual = mapper.mapToViewState(entry)

        assertThat(actual.actions?.updateAliasEnabled).isTrue()
        assertThat(actual.actions?.removeAliasEnabled).isTrue()
    }
}
