package io.alexeychurchill.dplayer.library.presentation

import io.alexeychurchill.dplayer.media.domain.FileMetadata
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test

class TrackInfoMapperTest {

    private val mapper = TrackInfoMapper()

    @Test
    fun `metadata is null`() {
        val actual = mapper.mapToTrackInfo(metadata = null)
        assertThat(actual).isNull()
    }

    @Test
    fun `metadata title is null`() {
        val metadata = FileMetadata(
            title = null,
            artist = "null",
            year = 1985,
        )

        val actual = mapper.mapToTrackInfo(metadata)

        assertThat(actual).isNull()
    }

    @Test
    fun `metadata title and artist are not null, year is null`() {
        val metadata = FileMetadata(
            title = "not null",
            artist = "null",
            year = null,
        )

        val actual = mapper.mapToTrackInfo(metadata)

        assertThat(actual).isEqualTo(
            SecondaryInfoViewState.TrackInfo(
                artist = "null",
                year = null,
            )
        )
    }

    @Test
    fun `title, artist and year are not null`() {
        val metadata = FileMetadata(
            title = "not null",
            artist = "null",
            year = 1986,
        )

        val actual = mapper.mapToTrackInfo(metadata)

        assertThat(actual).isEqualTo(
            SecondaryInfoViewState.TrackInfo(
                artist = "null",
                year = 1986,
            )
        )
    }
}
