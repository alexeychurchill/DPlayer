package io.alexeychurchill.dplayer.media.data

import android.content.Context
import androidx.media3.common.C.TRACK_TYPE_AUDIO
import androidx.media3.common.Format
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.exoplayer.MetadataRetriever
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.guava.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaMetadataStore @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    fun getCoverArtBytes(mediaUri: String): ByteArray? {
        return getMetadataBlocking(mediaUri).artworkData
    }

    @Suppress("UnsafeOptInUsageError")
    suspend fun retrieve(mediaUri: String): MediaMetadata {
        val mediaItem = MediaItem.Builder()
            .setUri(mediaUri)
            .build()

        val trackGroupArray = MetadataRetriever
            .retrieveMetadata(context, mediaItem)
            .await()

        val trackGroups = List(trackGroupArray.length) { index -> trackGroupArray[index] }
        val audioTrackGroups = trackGroups.filter { trackGroup ->
            trackGroup.type == TRACK_TYPE_AUDIO
        }

        val formatGroups = audioTrackGroups.flatMap { trackGroup ->
            List(trackGroup.length) { index -> trackGroup.getFormat(index) }
        }

        val metadata = formatGroups.mapNotNull(Format::metadata)
        return MediaMetadata.Builder()
            .populateFromMetadata(metadata)
            .build()
    }

    @Suppress("UnsafeOptInUsageError")
    private fun getMetadataBlocking(mediaUri: String): MediaMetadata {
        val mediaItem = MediaItem.Builder()
            .setUri(mediaUri)
            .build()

        val trackGroupArray = MetadataRetriever
            .retrieveMetadata(context, mediaItem)
            .get()

        val trackGroups = List(trackGroupArray.length) { index -> trackGroupArray[index] }
        val audioTrackGroups = trackGroups.filter { trackGroup ->
            trackGroup.type == TRACK_TYPE_AUDIO
        }

        val formatGroups = audioTrackGroups.flatMap { trackGroup ->
            List(trackGroup.length) { index -> trackGroup.getFormat(index) }
        }

        val metadata = formatGroups.mapNotNull(Format::metadata)
        return MediaMetadata.Builder()
            .populateFromMetadata(metadata)
            .build()
    }
}
