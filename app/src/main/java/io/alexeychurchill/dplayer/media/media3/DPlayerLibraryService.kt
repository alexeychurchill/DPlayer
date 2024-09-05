package io.alexeychurchill.dplayer.media.media3

import android.content.Intent
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DPlayerLibraryService : MediaLibraryService() {
    private var session: MediaLibrarySession? = null
    private val sessionCallback = object : MediaLibrarySession.Callback {
        /* TODO: Implement library browser */
    }

    override fun onCreate() {
        super.onCreate()

        val player = ExoPlayer.Builder(this)
            .build()

        session = MediaLibrarySession.Builder(this, player, sessionCallback)
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
