@file:OptIn(ExperimentalCoroutinesApi::class)

package io.alexeychurchill.dplayer.playback.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.alexeychurchill.dplayer.media.domain.PlaybackEngine
import io.alexeychurchill.dplayer.media.domain.PlaybackStatus
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val NoMetadataValue = "-"

private const val NoTimeValue = "--:--"

@HiltViewModel
class PlaybackViewModel @Inject constructor(
    private val playbackEngine: PlaybackEngine,
    private val playbackTimeMapper: PlaybackTimeMapper,
) : ViewModel() {

    private val seekInProgress: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private val seekingValue: MutableStateFlow<Float> = MutableStateFlow(0.0f)

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

    val totalTimeString: StateFlow<String> = playbackEngine
        .trackDuration
        .map { durationMs ->
            if (durationMs == null) return@map NoTimeValue
            playbackTimeMapper.mapToString(durationMs)
        }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(stopTimeoutMillis = 2_000L),
            initialValue = NoTimeValue,
        )

    val elapsedTimeString: StateFlow<String> = playbackEngine
        .trackDuration
        .combine(createTrackElapsedFlow()) { durationMs, elapsedMs ->
            if (durationMs == null || elapsedMs == null) return@combine NoTimeValue
            playbackTimeMapper.mapToStringLeadingHours(timeMs = elapsedMs, totalMs = durationMs)
        }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(stopTimeoutMillis = 2_000L),
            initialValue = NoTimeValue,
        )

    val trackProgress: StateFlow<Float?> = seekInProgress
        .flatMapLatest { isSeekInProgress ->
            if (isSeekInProgress) {
                seekingValue
            } else {
                createTrackProgressFlow()
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(stopTimeoutMillis = 2_000L),
            initialValue = null,
        )

    val playbackState: StateFlow<PlaybackStatus> = playbackEngine.playbackStatus

    fun playPause() {
        viewModelScope.launch {
            playbackEngine.togglePlayback()
        }
    }

    fun seek(progress: Float) {
        seekInProgress.tryEmit(true)
        seekingValue.tryEmit(progress)
    }

    fun stopSeeking() {
        viewModelScope.launch {
            seekInProgress.emit(false)
            val durationMs = playbackEngine.trackDuration.value ?: 0L
            val positionTimeMs = (seekingValue.value * durationMs).toLong()
            playbackEngine.seek(positionTimeMs)
        }
    }

    private fun createTrackProgressFlow(): Flow<Float?> = playbackEngine
        .trackDuration
        .combine(playbackEngine.trackElapsed) { duration, elapsed ->
            if (duration == null || elapsed == null) return@combine null
            elapsed.toFloat() / duration
        }

    private fun createTrackElapsedFlow(): Flow<Long?> = seekInProgress
        .flatMapLatest { isSeekInProgress ->
            if (isSeekInProgress) {
                playbackEngine
                    .trackDuration
                    .combine(seekingValue) { durationMs, seekProgress ->
                        durationMs?.let { it * seekProgress }?.toLong()
                    }
            } else {
                playbackEngine.trackElapsed
            }
        }
}
