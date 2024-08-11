package io.alexeychurchill.dplayer.library.presentation.mapper

import io.alexeychurchill.dplayer.core.domain.filesystem.FileName.Companion.DefaultUnknownValue
import io.alexeychurchill.dplayer.core.domain.filesystem.FileName.Name
import io.alexeychurchill.dplayer.core.domain.filesystem.FileName.Unknown
import io.alexeychurchill.dplayer.core.domain.filesystem.FileSystemEntry.Directory
import io.alexeychurchill.dplayer.core.domain.filesystem.FileSystemEntry.File
import io.alexeychurchill.dplayer.library.domain.MediaEntry
import io.alexeychurchill.dplayer.library.presentation.model.LibraryAction.OpenMediaEntry
import io.alexeychurchill.dplayer.library.presentation.model.FileEntryViewState
import io.alexeychurchill.dplayer.media.domain.FileMetadata
import io.alexeychurchill.dplayer.media.presentation.CoverArtPath.LocalUri
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class FileEntryViewStateMapperTest {

    private val mapper = FileEntryViewStateMapper()

    @Test
    fun `directory file system entry mapping is failing`() {
        val entry = MediaEntry(
            fsEntry = Directory(path = "", name = Unknown, exists = true),
        )

        assertThrows<IllegalArgumentException> {
            mapper.mapToViewState(entry, metadata = null)
        }
    }

    @Test
    fun `all fields mapped correctly for all present data`() {
        val entry = MediaEntry(
            fsEntry = File(path = "path", name = Name("fname"), extension = "ext"),
        )
        val metadata = FileMetadata(
            title = "title",
            artist = "artist",
            album = "album",
            genre = "genre",
            year = 2000,
        )

        val actual = mapper.mapToViewState(entry, metadata = metadata)

        assertThat(actual).isEqualTo(
            FileEntryViewState(
                path = "path",
                visibleTitle = "title",
                artist = "artist",
                year = 2000,
                fileExtension = "ext",
                coverArtPath = LocalUri("path"),
                openAction = OpenMediaEntry.File("path"),
            )
        )
    }

    @Test
    fun `metadata is empty`() {
        val entry = MediaEntry(
            fsEntry = File(path = "path", name = Name("fname"), extension = "ext"),
        )
        val metadata = FileMetadata()

        val actual = mapper.mapToViewState(entry, metadata = metadata)

        assertThat(actual).isEqualTo(
            FileEntryViewState(
                path = "path",
                visibleTitle = "fname",
                artist = null,
                year = null,
                fileExtension = "ext",
                coverArtPath = LocalUri("path"),
                openAction = OpenMediaEntry.File("path"),
            )
        )
    }

    @Test
    fun `metadata is null`() {
        val entry = MediaEntry(
            fsEntry = File(path = "path", name = Name("fname"), extension = "ext"),
        )

        val actual = mapper.mapToViewState(entry, metadata = null)

        assertThat(actual).isEqualTo(
            FileEntryViewState(
                path = "path",
                visibleTitle = "fname",
                artist = null,
                year = null,
                fileExtension = "ext",
                coverArtPath = LocalUri("path"),
                openAction = OpenMediaEntry.File("path"),
            )
        )
    }

    @Test
    fun `metadata is null, name is Unknown, no extension`() {
        val entry = MediaEntry(
            fsEntry = File(path = "path", name = Unknown, extension = null),
        )

        val actual = mapper.mapToViewState(entry, metadata = null)

        assertThat(actual).isEqualTo(
            FileEntryViewState(
                path = "path",
                visibleTitle = DefaultUnknownValue,
                artist = null,
                year = null,
                fileExtension = null,
                coverArtPath = LocalUri("path"),
                openAction = OpenMediaEntry.File("path"),
            )
        )
    }

    @Test
    fun `metadata has title, name is Unknown`() {
        val entry = MediaEntry(
            fsEntry = File(path = "path", name = Unknown, extension = "ext"),
        )
        val metadata = FileMetadata(title = "title")

        val actual = mapper.mapToViewState(entry, metadata = metadata)

        assertThat(actual).isEqualTo(
            FileEntryViewState(
                path = "path",
                visibleTitle = "title",
                artist = null,
                year = null,
                fileExtension = "ext",
                coverArtPath = LocalUri("path"),
                openAction = OpenMediaEntry.File("path"),
            )
        )
    }
}
