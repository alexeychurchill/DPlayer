package io.alexeychurchill.dplayer.library.presentation

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class FileSectionBuilderTest {

    private val mediaEntryMapperMock: MediaEntryViewStateMapper = mockk {
        every { mapToViewState(any()) } returns MediaEntryItemViewState(
            path = "",
            title = "",
            type = MediaEntryItemViewState.Type.MusicFile,
        )
    }

    private val builder = FileSectionBuilder(
        mediaEntryMapper = mediaEntryMapperMock,
    )

    @Test
    fun `files present`() {
        val testData = TestMediaEntries.mixed
        val fileCount = TestMediaEntries.fileMediaEntries.size
        val actual = builder.build(testData)

        assertThat(actual)
            .first()
            .isEqualTo(LibrarySectionViewState.Header.ForFiles)

        assertThat(actual)
            .element(1)
            .matches {
                it is LibrarySectionViewState.MediaEntries && it.items.size == fileCount
            }
    }

    @Test
    fun `no directories`() {
        val testData = TestMediaEntries.directoryMediaEntries
        val actual = builder.build(testData)

        assertThat(actual).first().isEqualTo(LibrarySectionViewState.Header.ForFiles)
        assertThat(actual).element(1).isEqualTo(LibrarySectionViewState.FilesAbsent)
    }
}
