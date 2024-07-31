package io.alexeychurchill.dplayer.library.presentation

import io.alexeychurchill.dplayer.core.domain.filesystem.FileName
import io.alexeychurchill.dplayer.core.domain.filesystem.FileSystemEntry
import io.alexeychurchill.dplayer.library.domain.EntrySource
import io.alexeychurchill.dplayer.library.domain.MediaEntry
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime


class MediaEntryTitleMapperTest {

    private companion object {
        val directoryPrototype = MediaEntry.Directory(
            directoryEntry = FileSystemEntry.Directory(
                path = "", name = FileName.Unknown, exists = true
            ),
            subDirectoryCount = 0,
            musicFileCount = 0,
            source = EntrySource.FileSystem,
        )
    }

    private val mapper = MediaEntryTitleMapper()

    @Test
    fun `directory from user library with alias name`() {
        val name = "Original Name"
        val alias = "Alias Name"
        val mediaEntry = directoryPrototype.copy(
            directoryEntry = directoryPrototype.directoryEntry?.copy(name = FileName.Name(name)),
            source = EntrySource.UserLibrary(
                aliasTitle = alias,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
            ),
        )

        val actualTitle = mapper.mapToTitle(mediaEntry)

        assertThat(actualTitle).isEqualTo(alias)
    }

    @Test
    fun `directory from user library with alias name and unknown name`() {
        val alias = "Alias Name"
        val mediaEntry = directoryPrototype.copy(
            directoryEntry = directoryPrototype.directoryEntry?.copy(name = FileName.Unknown),
            source = EntrySource.UserLibrary(
                aliasTitle = alias,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
            ),
        )

        val actualTitle = mapper.mapToTitle(mediaEntry)

        assertThat(actualTitle).isEqualTo(alias)
    }

    @Test
    fun `directory from user library without alias name`() {
        val name = "Original Name"
        val mediaEntry = directoryPrototype.copy(
            directoryEntry = directoryPrototype.directoryEntry?.copy(name = FileName.Name(name)),
            source = EntrySource.UserLibrary(
                aliasTitle = null,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
            ),
        )

        val actualTitle = mapper.mapToTitle(mediaEntry)

        assertThat(actualTitle).isEqualTo(name)
    }

    @Test
    fun `directory from file system`() {
        val name = "Original Name"
        val mediaEntry = directoryPrototype.copy(
            directoryEntry = directoryPrototype.directoryEntry?.copy(name = FileName.Name(name)),
            source = EntrySource.FileSystem,
        )

        val actualTitle = mapper.mapToTitle(mediaEntry)

        assertThat(actualTitle).isEqualTo(name)
    }

    @Test
    fun `directory has unknown name`() {
        val mediaEntry = directoryPrototype.copy(
            directoryEntry = directoryPrototype.directoryEntry?.copy(name = FileName.Unknown),
            source = EntrySource.FileSystem,
        )

        val actualTitle = mapper.mapToTitle(mediaEntry)

        assertThat(actualTitle).isEqualTo(FileName.DefaultUnknownValue)
    }

    @Test
    fun `file has known name`() {
        val name = "Known name"
        val mediaEntry = MediaEntry.File(
            fileEntry = FileSystemEntry.File(
                path = "", name = FileName.Name(name), extension = null
            )
        )

        val actualTitle = mapper.mapToTitle(mediaEntry)

        assertThat(actualTitle).isEqualTo(name)
    }

    @Test
    fun `file has unknown name`() {
        val mediaEntry = MediaEntry.File(
            fileEntry = FileSystemEntry.File(path = "", name = FileName.Unknown, extension = null)
        )

        val actualTitle = mapper.mapToTitle(mediaEntry)

        assertThat(actualTitle).isEqualTo(FileName.DefaultUnknownValue)
    }
}

