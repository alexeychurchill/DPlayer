package io.alexeychurchill.dplayer.media.media3

import android.content.Context
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import dagger.hilt.android.qualifiers.ApplicationContext
import io.alexeychurchill.dplayer.core.domain.MediaId
import io.alexeychurchill.dplayer.media.domain.PlaybackEngine
import io.alexeychurchill.dplayer.media.domain.PlaybackEngineState
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
        if (playbackState == Player.STATE_READY && !playWhenReady || playbackState == Player.STATE_ENDED) {
            playWhenReady = true
        }
    }

    override fun pause() = with(player) {
        if (playbackState == Player.STATE_READY && playWhenReady) {
            playWhenReady = false
        }
    }

    override fun togglePlayback() {
        if (player.playbackState != Player.STATE_READY) {
            return
        }
        player.playWhenReady = !player.playWhenReady
    }

    override fun seek(positionMs: Long) {
        if (player.playbackState == Player.STATE_IDLE || player.playbackState == Player.STATE_BUFFERING) {
            return
        }
        player.seekTo(positionMs)
    }
}