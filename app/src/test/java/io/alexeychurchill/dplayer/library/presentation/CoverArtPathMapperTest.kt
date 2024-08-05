package io.alexeychurchill.dplayer.library.presentation

import io.alexeychurchill.dplayer.core.domain.filesystem.FileName
import io.alexeychurchill.dplayer.core.domain.filesystem.FileSystemEntry
import io.alexeychurchill.dplayer.library.domain.MediaEntry
import io.alexeychurchill.dplayer.media.presentation.CoverArtPath
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CoverArtPathMapperTest {

    private val mapper = CoverArtPathMapper()

    @Test
    fun `cover art path for file entry`() {
        val path = "path"
        val entry = MediaEntry(
            fsEntry = FileSystemEntry.File(
                path = path,
                name = FileName.of(""),
                extension = null,
            ),
        )

        val actual = mapper.mapToCoverArtPath(entry)

        assertThat(actual).isEqualTo(CoverArtPath.LocalUri(mediaUri = path))
    }

    @Test
    fun `cover art path for non-file entry`() {
        val entry = MediaEntry(
            fsEntry = FileSystemEntry.Directory(
                path = "",
                name = FileName.of(""),
                exists = true,
            ),
        )

        val actual = mapper.mapToCoverArtPath(entry)

        assertThat(actual).isNull()
    }
}
