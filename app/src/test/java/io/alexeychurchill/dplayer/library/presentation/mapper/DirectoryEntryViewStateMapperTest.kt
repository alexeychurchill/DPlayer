package io.alexeychurchill.dplayer.library.presentation.mapper

import io.alexeychurchill.dplayer.core.domain.filesystem.FileName.Companion.DefaultUnknownValue
import io.alexeychurchill.dplayer.core.domain.filesystem.FileName.Companion.of
import io.alexeychurchill.dplayer.core.domain.filesystem.FileName.Unknown
import io.alexeychurchill.dplayer.core.domain.filesystem.FileSystemEntry.Directory
import io.alexeychurchill.dplayer.core.domain.filesystem.FileSystemEntry.File
import io.alexeychurchill.dplayer.library.domain.EntryInfo
import io.alexeychurchill.dplayer.library.domain.EntrySource
import io.alexeychurchill.dplayer.library.domain.MediaEntry
import io.alexeychurchill.dplayer.library.presentation.LibraryAction.OpenMediaEntry
import io.alexeychurchill.dplayer.library.presentation.OpenDirectoryPayload
import io.alexeychurchill.dplayer.library.presentation.viewstate.DirectoryEntryViewState
import io.alexeychurchill.dplayer.library.presentation.viewstate.DirectoryStatusViewState.None
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime

class DirectoryEntryViewStateMapperTest {

    private val statusMapperMock = mockk<DirectoryStatusViewStateMapper> {
        every { mapToViewState(any()) } returns None
    }

    private val mapper = DirectoryEntryViewStateMapper(
        statusMapper = statusMapperMock,
    )

    @Test
    fun `entry is not a directory entry`() {
        val entry = MediaEntry(
            fsEntry = File(path = "", name = Unknown, extension = null),
        )

        assertThrows<IllegalArgumentException> {
            mapper.mapToViewState(entry)
        }
    }

    @Test
    fun `entry has all required data, alias is set`() {
        val entry = MediaEntry(
            fsEntry = Directory(path = "path", name = of(name = "dname"), exists = true),
            source = EntrySource.UserLibrary(
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
                aliasTitle = "alias",
            ),
            info = EntryInfo.Directory(
                directoryCount = 1,
                musicFileCount = 2,
            ),
        )

        val actual = mapper.mapToViewState(entry)

        assertThat(actual).isEqualTo(
            DirectoryEntryViewState(
                path = "path",
                visibleTitle = "alias",
                status = None,
                fileCount = 2,
                directoryCount = 1,
                openAction = OpenMediaEntry.Directory(
                    payload = OpenDirectoryPayload(
                        title = "alias",
                        path = "path",
                    ),
                ),
            )
        )
    }

    @Test
    fun `entry has no directory info, alias is not set`() {
        val entry = MediaEntry(
            fsEntry = Directory(path = "path", name = of(name = "dname"), exists = true),
            source = EntrySource.UserLibrary(
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
                aliasTitle = null,
            ),
            info = EntryInfo.None,
        )

        val actual = mapper.mapToViewState(entry)

        assertThat(actual).isEqualTo(
            DirectoryEntryViewState(
                path = "path",
                visibleTitle = "dname",
                status = None,
                fileCount = null,
                directoryCount = null,
                openAction = OpenMediaEntry.Directory(
                    payload = OpenDirectoryPayload(
                        title = "dname",
                        path = "path",
                    ),
                ),
            )
        )
    }

    @Test
    fun `entry has no directory info, alias is set, dir name is unknown`() {
        val entry = MediaEntry(
            fsEntry = Directory(path = "path", name = Unknown, exists = true),
            source = EntrySource.UserLibrary(
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
                aliasTitle = "alias",
            ),
            info = EntryInfo.None,
        )

        val actual = mapper.mapToViewState(entry)

        assertThat(actual).isEqualTo(
            DirectoryEntryViewState(
                path = "path",
                visibleTitle = "alias",
                status = None,
                fileCount = null,
                directoryCount = null,
                openAction = OpenMediaEntry.Directory(
                    payload = OpenDirectoryPayload(
                        title = "alias",
                        path = "path",
                    ),
                ),
            )
        )
    }

    @Test
    fun `entry has no directory info, is not from user library`() {
        val entry = MediaEntry(
            fsEntry = Directory(path = "path", name = of(name = "dname"), exists = true),
            source = EntrySource.FileSystem,
            info = EntryInfo.None,
        )

        val actual = mapper.mapToViewState(entry)

        assertThat(actual).isEqualTo(
            DirectoryEntryViewState(
                path = "path",
                visibleTitle = "dname",
                status = None,
                fileCount = null,
                directoryCount = null,
                openAction = OpenMediaEntry.Directory(
                    payload = OpenDirectoryPayload(
                        title = "dname",
                        path = "path",
                    ),
                ),
            )
        )
    }

    @Test
    fun `entry has no directory info, is not from user library, has unknown name`() {
        val entry = MediaEntry(
            fsEntry = Directory(path = "path", name = Unknown, exists = true),
            source = EntrySource.FileSystem,
            info = EntryInfo.None,
        )

        val actual = mapper.mapToViewState(entry)

        assertThat(actual).isEqualTo(
            DirectoryEntryViewState(
                path = "path",
                visibleTitle = DefaultUnknownValue,
                status = None,
                fileCount = null,
                directoryCount = null,
                openAction = OpenMediaEntry.Directory(
                    payload = OpenDirectoryPayload(
                        title = DefaultUnknownValue,
                        path = "path",
                    ),
                ),
            )
        )
    }
}
