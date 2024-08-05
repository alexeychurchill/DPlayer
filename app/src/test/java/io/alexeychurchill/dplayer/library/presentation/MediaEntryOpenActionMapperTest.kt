package io.alexeychurchill.dplayer.library.presentation

import io.alexeychurchill.dplayer.core.domain.filesystem.FileName
import io.alexeychurchill.dplayer.core.domain.filesystem.FileSystemEntry
import io.alexeychurchill.dplayer.library.domain.MediaEntry
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test

class MediaEntryOpenActionMapperTest {

    private companion object {
        const val DirectoryTitle = "DirName"
    }

    private val titleMapperMock: MediaEntryTitleMapper = mockk {
        every { mapToTitle(any(), any()) } returns DirectoryTitle
    }

    private val mapper = MediaEntryOpenActionMapper(
        titleMapper = titleMapperMock,
    )

    @Test
    fun `open action for directory calls title mapper and returns mapped title`() {
        val path = "path"
        val entry = MediaEntry(
            fsEntry = FileSystemEntry.Directory(
                path = path,
                name = FileName.Name(""),
                exists = true,
            ),
        )

        val actual = mapper.mapToOpenAction(entry)

        assertThat(actual).isEqualTo(
            LibraryAction.OpenMediaEntry.Directory(
                payload = OpenDirectoryPayload(
                    path = path,
                    title = DirectoryTitle,
                ),
            )
        )

        verify { titleMapperMock.mapToTitle(entry, metadata = null) }
    }

    @Test
    fun `open action for file`() {
        val path = "path"
        val entry = MediaEntry(
            fsEntry = FileSystemEntry.File(
                path = path,
                name = FileName.Name(""),
                extension = null,
            ),
        )

        val actual = mapper.mapToOpenAction(entry)

        assertThat(actual).isEqualTo(LibraryAction.OpenMediaEntry.File(path))
    }
}
