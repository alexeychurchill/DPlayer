package io.alexeychurchill.dplayer.media.media3

import android.content.Intent
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import dagger.hilt.android.AndroidEntryPoint
import io.alexeychurchill.dplayer.MainActivity
import io.alexeychurchill.dplayer.core.Intents

@AndroidEntryPoint
class DPlayerLibraryService : MediaLibraryService() {

    companion object {
        private const val RequestCodeOpenPlayback = 20001
    }

    private var session: MediaLibrarySession? = null
    private val sessionCallback = object : MediaLibrarySession.Callback {
        /* TODO: Implement library browser */
    }

    override fun onCreate() {
        super.onCreate()

        val player = ExoPlayer.Builder(this)
            .build()

        val pendingOpenPlayback = Intents.pendingOpenPlayback<MainActivity>(
            context = this,
            requestCode = RequestCodeOpenPlayback,
        )

        session = MediaLibrarySession.Builder(this, player, sessionCallback)
            .setSessionActivity(pendingOpenPlayback)
            .build()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaLibrarySession? {
        return session
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        /* TODO: Consider changing this behavior! */
        session?.run {
            if (player.playWhenReady) {
                player.pause()
            }
        }
        stopSelf()
    }

    override fun onDestroy() {
        session?.run {
            player.release()
            release()
            session = null
        }
        super.onDestroy()
    }
}
