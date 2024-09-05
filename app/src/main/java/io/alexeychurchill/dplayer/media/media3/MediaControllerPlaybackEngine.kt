package io.alexeychurchill.dplayer.media.media3

import android.content.ComponentName
import android.content.Context
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import dagger.hilt.android.qualifiers.ApplicationContext
import io.alexeychurchill.dplayer.core.domain.MediaId
import io.alexeychurchill.dplayer.media.domain.PlaybackEngine
import io.alexeychurchill.dplayer.media.domain.PlaybackEngineState
import kotlinx.coroutines.guava.await
import javax.inject.Inject
import javax.inject.Singleton

/**
 * TODO: Consider adding of the supported commands checks
 */
@Singleton
class MediaControllerPlaybackEngine @Inject constructor(
    @ApplicationContext private val context: Context,
    private val playerListener: PlaybackEngineStateListener,
) : DefaultLifecycleObserver, PlaybackEngine, PlaybackEngineState by playerListener {

    private var mediaController: ListenableFuture<MediaController>? = null

    // Lifecycle

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)

        val componentName = ComponentName(context, DPlayerLibraryService::class.java)
        val sessionToken = SessionToken(context, componentName)
        mediaController = MediaController.Builder(context, sessionToken)
            .buildAsync()

        mediaController?.run {
            addListener(
                { playerListener.attachPlayer(get()) },
                MoreExecutors.directExecutor(),
            )
        }
    }

    override fun onStop(owner: LifecycleOwner) {
        mediaController?.run {
            playerListener.detachPlayer()
            MediaController.releaseFuture(this)
            mediaController = null
        }
        super.onStop(owner)
    }

    // Playback engine

    override suspend fun use(mediaId: MediaId) = withPlayer {
        if (mediaId !is MediaId.Local) return@withPlayer
        val mediaItem = MediaItem.Builder()
            .setUri(mediaId.uri)
            .build()
        setMediaItem(mediaItem)
        prepare()
    }

    override suspend fun play() = withPlayer {
        if (playbackState == Player.STATE_READY && !playWhenReady || playbackState == Player.STATE_ENDED) {
            playWhenReady = true
        }
    }

    override suspend fun pause() = withPlayer {
        if (playbackState == Player.STATE_READY && playWhenReady) {
            playWhenReady = false
        }
    }

    override suspend fun togglePlayback() = withPlayer {
        if (playbackState != Player.STATE_READY) {
            return@withPlayer
        }
        playWhenReady = !playWhenReady
    }

    override suspend fun seek(positionMs: Long) = withPlayer {
        if (playbackState == Player.STATE_IDLE || playbackState == Player.STATE_BUFFERING) {
            return@withPlayer
        }
        seekTo(positionMs)
    }

    private suspend fun withPlayer(block: Player.() -> Unit) {
        val controller = mediaController ?: return
        controller.await().block()
    }
}
