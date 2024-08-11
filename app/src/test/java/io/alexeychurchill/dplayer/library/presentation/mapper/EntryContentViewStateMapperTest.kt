package io.alexeychurchill.dplayer.library.presentation.mapper

import io.alexeychurchill.dplayer.library.presentation.TestMediaEntries.directoryMediaEntries
import io.alexeychurchill.dplayer.library.presentation.TestMediaEntries.fileMediaEntries
import io.alexeychurchill.dplayer.library.presentation.TestMediaEntries.mixed
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.Test

class EntryContentViewStateMapperTest {

    private val directoryMapperMock = mockk<DirectoryEntryViewStateMapper>(relaxed = true)
    private val fileMapperMock = mockk<FileEntryViewStateMapper>(relaxed = true)

    private val mapper = EntryContentViewStateMapper(
        directoryMapper = directoryMapperMock,
        fileMapper = fileMapperMock,
    )

    @Test
    fun `only file entries`() {
        val actual = mapper.mapToViewState(fileMediaEntries)

        verify(exactly = fileMediaEntries.size) { fileMapperMock.mapToViewState(any(), any()) }
        verify(exactly = 0) { directoryMapperMock.mapToViewState(any()) }
        assertThat(actual.filesSectionHeaderPresent).isTrue()
        assertThat(actual.noFilesHeaderPresent).isFalse()
        assertThat(actual.directorySectionHeaderPresent).isFalse()
    }

    @Test
    fun `only directory entries`() {
        val actual = mapper.mapToViewState(directoryMediaEntries)

        verify(exactly = 0) { fileMapperMock.mapToViewState(any(), any()) }
        verify(exactly = directoryMediaEntries.size) { directoryMapperMock.mapToViewState(any()) }
        assertThat(actual.filesSectionHeaderPresent).isTrue()
        assertThat(actual.noFilesHeaderPresent).isTrue()
        assertThat(actual.directorySectionHeaderPresent).isTrue()
    }

    @Test
    fun `file and directory entries`() {
        val actual = mapper.mapToViewState(mixed)

        verify(exactly = fileMediaEntries.size) { fileMapperMock.mapToViewState(any(), any()) }
        verify(exactly = directoryMediaEntries.size) { directoryMapperMock.mapToViewState(any()) }
        assertThat(actual.filesSectionHeaderPresent).isTrue()
        assertThat(actual.noFilesHeaderPresent).isFalse()
        assertThat(actual.directorySectionHeaderPresent).isTrue()
    }

    @Test
    fun `no entries at all`() {
        val actual = mapper.mapToViewState(emptyList())

        verify(exactly = 0) { fileMapperMock.mapToViewState(any(), any()) }
        verify(exactly = 0) { directoryMapperMock.mapToViewState(any()) }
        assertThat(actual.filesSectionHeaderPresent).isTrue()
        assertThat(actual.noFilesHeaderPresent).isTrue()
        assertThat(actual.directorySectionHeaderPresent).isFalse()
    }
}
