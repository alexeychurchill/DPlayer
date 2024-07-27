package io.alexeychurchill.clown.core.domain.filesystem

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class FileSystemEntryExtTest {

    private companion object {

        val FilesTestData = listOf(
            FileSystemEntry.File(path = "", name = FileName.Name(value = "abc.mp3")),
            FileSystemEntry.File(path = "", name = FileName.Name(value = "cde.aiff")),
            FileSystemEntry.File(path = "", name = FileName.Name(value = "def.mp3")),
            FileSystemEntry.File(path = "", name = FileName.Name(value = "efg.wav")),
            FileSystemEntry.File(path = "", name = FileName.Name(value = "fgs.mp3")),
        )

        val DirectoriesTestData = listOf(
            FileSystemEntry.Directory(path = "", name = FileName.Unknown, exists = true),
            FileSystemEntry.Directory(path = "", name = FileName.Unknown, exists = true),
            FileSystemEntry.Directory(path = "", name = FileName.Unknown, exists = true),
        )

        val TestData = FilesTestData + DirectoriesTestData
    }

    @Test
    fun `file count with no extension predicate - 5 files, 3 dirs`() {
        val actual = TestData.fileCount()
        assertThat(actual).isEqualTo(5)
    }

    @Test
    fun `file count with extension predicate - 3 out of 5 files, 3 dirs`() {
        val actual = TestData.fileCount { extension -> extension == "mp3" }
        assertThat(actual).isEqualTo(3)
    }

    @Test
    fun `file count with no extension predicate - 0 files, 3 dirs`() {
        val actual = DirectoriesTestData.fileCount()
        assertThat(actual).isEqualTo(0)
    }

    @Test
    fun `directory count - 3 dirs out of 8 entries`() {
        val actual = TestData.directoryCount()
        assertThat(actual).isEqualTo(3)
    }

    @Test
    fun `directory count - 0 dirs out of 5 entries`() {
        val actual = FilesTestData.directoryCount()
        assertThat(actual).isEqualTo(0)
    }

    @Test
    fun `directory count - 0 dirs out of 0 entries`() {
        val actual = emptyList<FileSystemEntry>().directoryCount()
        assertThat(actual).isEqualTo(0)
    }
}
