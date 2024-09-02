package io.alexeychurchill.dplayer.playback.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.alexeychurchill.dplayer.engine.PlaybackEngine
import io.alexeychurchill.dplayer.engine.PlaybackStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

private const val NoMetadataValue = "-"

@HiltViewModel
class PlaybackViewModel @Inject constructor(
    private val playbackEngine: PlaybackEngine,
) : ViewModel() {

    val trackTitle: StateFlow<String?> = playbackEngine
        .fileMetadata
        .map { if (it == null) NoMetadataValue else it.title }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = null,
        )

    val artistName: StateFlow<String?> = playbackEngine
        .fileMetadata
        .map { if (it == null) NoMetadataValue else it.artist }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = null,
        )

    val elapsedTimeString: StateFlow<String> = MutableStateFlow("000:00:00")

    val totalTimeString: StateFlow<String> = MutableStateFlow("000:00:00")

    val trackProgress: StateFlow<Float> = MutableStateFlow(0.0f)

    val playbackState: StateFlow<PlaybackStatus> = playbackEngine.playbackStatus

    fun togglePlayPause() {
        playbackEngine.togglePlayback()
    }
}
