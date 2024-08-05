package io.alexeychurchill.dplayer.media.data

import androidx.media3.common.MediaMetadata
import io.alexeychurchill.dplayer.media.domain.FileMetadata
import javax.inject.Inject

class MediaMetadataMapper @Inject constructor() {

    fun mapToFileMetadata(mediaMetadata: MediaMetadata): FileMetadata {
        return FileMetadata(
            title = mediaMetadata.title?.toString(),
            artist = mediaMetadata.artist?.toString(),
            album = mediaMetadata.albumTitle?.toString(),
            genre = mediaMetadata.genre?.toString(),
            year = mediaMetadata.releaseYear,
        )
    }
}
