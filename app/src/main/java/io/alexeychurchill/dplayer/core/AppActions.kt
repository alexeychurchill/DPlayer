package io.alexeychurchill.dplayer.core

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import io.alexeychurchill.dplayer.BuildConfig

object Actions {
    const val OpenPlayback = "${BuildConfig.APPLICATION_ID}.OPEN_PLAYBACK"
}

object Intents {

    inline fun <reified T : Activity> pendingOpenPlayback(
        context: Context,
        requestCode: Int,
    ): PendingIntent {
        val intent = Intent(context, T::class.java).apply {
            action = Actions.OpenPlayback
        }
        return PendingIntent.getActivity(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_CANCEL_CURRENT,
        )
    }
}
