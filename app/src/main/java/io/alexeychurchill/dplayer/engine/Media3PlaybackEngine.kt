package io.alexeychurchill.dplayer.engine

import android.content.Context
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.Player.STATE_ENDED
import androidx.media3.common.Player.STATE_READY
import androidx.media3.exoplayer.ExoPlayer
import dagger.hilt.android.qualifiers.ApplicationContext
import io.alexeychurchill.dplayer.core.domain.MediaId
import javax.inject.Inject
import javax.inject.Singleton

/**
 * TODO: Check commands
 */
@Singleton
class Media3PlaybackEngine @Inject constructor(
    @ApplicationContext private val context: Context,
    private val playerListener: PlaybackEngineStateListener,
) : DefaultLifecycleObserver, PlaybackEngine, PlaybackEngineState by playerListener {

    private lateinit var player: Player

    // Lifecycle

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        player = ExoPlayer.Builder(context)
            .build()
        player.playWhenReady = true
        playerListener.attachPlayer(player)
    }

    override fun onStop(owner: LifecycleOwner) {
        playerListener.detachPlayer()
        player.release()
        super.onStop(owner)
    }

    // Playback engine

    override suspend fun use(mediaId: MediaId) {
        if (mediaId !is MediaId.Local) return
        val mediaItem = MediaItem.Builder()
            .setUri(mediaId.uri)
            .build()
        player.setMediaItem(mediaItem)
        player.prepare()
    }

    override fun play() = with(player) {
        if (playbackState == STATE_READY && !playWhenReady || playbackState == STATE_ENDED) {
            playWhenReady = true
        }
    }

    override fun pause() = with(player) {
        if (playbackState == STATE_READY && playWhenReady) {
            playWhenReady = false
        }
    }

    override fun togglePlayback() {
        if (player.playbackState != STATE_READY) {
            return
        }
        player.playWhenReady = !player.playWhenReady
    }
}
