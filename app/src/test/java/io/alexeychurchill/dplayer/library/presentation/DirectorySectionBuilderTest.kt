package io.alexeychurchill.dplayer.library.presentation

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DirectorySectionBuilderTest {

    private val mediaEntryMapperMock: MediaEntryViewStateMapper = mockk {
        every { mapToViewState(any()) } returns MediaEntryItemViewState(
            path = null,
            title = "",
            type = MediaEntryItemViewState.Type.Directory,
            status = MediaEntryItemViewState.Status.Openable,
        )
    }

    private val builder = DirectorySectionBuilder(
        mediaEntryMapper = mediaEntryMapperMock,
    )

    @Test
    fun `directories present`() {
        val testData = TestMediaEntries.mixed
        val dirCount = TestMediaEntries.directoryMediaEntries.size
        val actual = builder.build(testData)

        assertThat(actual)
            .first()
            .isEqualTo(LibrarySectionViewState.Header.ForDirectories)

        assertThat(actual)
            .element(1)
            .matches {
                it is LibrarySectionViewState.MediaEntries && it.items.size == dirCount
            }
    }

    @Test
    fun `no directories`() {
        val testData = TestMediaEntries.fileMediaEntries
        val actual = builder.build(testData)

        assertThat(actual).isEmpty()
    }
}
