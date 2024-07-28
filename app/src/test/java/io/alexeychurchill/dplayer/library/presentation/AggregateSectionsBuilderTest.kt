package io.alexeychurchill.dplayer.library.presentation

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class AggregateSectionsBuilderTest {

    private companion object {

        val TestDirSection = listOf<LibrarySectionViewState>(
            LibrarySectionViewState.Header.ForDirectories,
        )

        val TestFileSection = listOf<LibrarySectionViewState>(
            LibrarySectionViewState.Header.ForFiles,
        )
    }

    private val directorySectionBuilderMock: DirectorySectionBuilder = mockk {
        every { build(any()) } returns TestDirSection
    }
    private val fileSectionBuilderMock: FileSectionBuilder = mockk {
        every { build(any()) } returns TestFileSection
    }

    private val builder = AggregateSectionsBuilder(
        directorySectionBuilder = directorySectionBuilderMock,
        fileSectionBuilder = fileSectionBuilderMock,
    )

    @Test
    fun `builder calls sections builders and returns grouped result`() {
        val testEntries = TestMediaEntries.mixed
        val result = builder.build(testEntries)

        verify {
            directorySectionBuilderMock.build(testEntries)
            fileSectionBuilderMock.build(testEntries)
        }

        assertThat(result).containsExactly(
            LibrarySectionViewState.Header.ForDirectories,
            LibrarySectionViewState.Header.ForFiles,
        )
    }
}
