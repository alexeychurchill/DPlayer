package io.alexeychurchill.dplayer.playback.presentation

import java.time.Duration
import java.util.Locale
import javax.inject.Inject

class PlaybackTimeMapper @Inject constructor() {

    fun mapToString(timeMs: Long): String {
        val (hours, minutes, seconds) = duration(timeMs)
        return if (hours > 0) {
            formatHoursMinutesSeconds(hours, minutes, seconds)
        } else {
            formatMinutesSeconds(minutes, seconds)
        }
    }

    fun mapToStringLeadingHours(timeMs: Long, totalMs: Long): String {
        val (totalHours, _, _) = duration(totalMs)
        val (hours, minutes, seconds) = duration(timeMs)
        return if (totalHours > 0) {
            formatHoursMinutesSeconds(hours, minutes, seconds)
        } else {
            formatMinutesSeconds(minutes, seconds)
        }
    }

    private fun duration(timeMs: Long): Triple<Long, Long, Long> {
        val duration = Duration.ofMillis(timeMs)
        val seconds = duration.seconds % 60
        val minutes = duration.toMinutes() % 60
        val hours = duration.toHours() % 24
        return Triple(hours, minutes, seconds)
    }

    private fun formatMinutesSeconds(minutes: Long, seconds: Long): String {
        return String.format(Locale.ROOT, "%02d:%02d", minutes, seconds)
    }

    private fun formatHoursMinutesSeconds(hours: Long, minutes: Long, seconds: Long): String {
        return String.format(Locale.ROOT, "%03d:%02d:%02d", hours, minutes, seconds)
    }
}
