package io.alexeychurchill.dplayer.playback.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.alexeychurchill.dplayer.engine.PlaybackEngine
import io.alexeychurchill.dplayer.engine.PlaybackStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PlaybackViewModel @Inject constructor(
    private val playbackEngine: PlaybackEngine,
) : ViewModel() {

    val trackTitle: StateFlow<String?> = MutableStateFlow(null)

    val trackArtist: StateFlow<String?> = MutableStateFlow(null)

    val elapsedTimeString: StateFlow<String> = MutableStateFlow("000:00:00")

    val totalTimeString: StateFlow<String> = MutableStateFlow("000:00:00")

    val trackProgress: StateFlow<Float> = MutableStateFlow(0.0f)

    val playbackState: StateFlow<PlaybackStatus> = playbackEngine.playbackStatus

    fun togglePlayPause() {
        playbackEngine.togglePlayback()
    }
}
