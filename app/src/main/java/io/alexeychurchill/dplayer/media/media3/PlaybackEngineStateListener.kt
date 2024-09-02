package io.alexeychurchill.dplayer.media.media3

import androidx.media3.common.C
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.Timeline
import io.alexeychurchill.dplayer.media.data.MediaMetadataMapper
import io.alexeychurchill.dplayer.media.domain.FileMetadata
import io.alexeychurchill.dplayer.media.domain.PlaybackEngineState
import io.alexeychurchill.dplayer.media.domain.PlaybackStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.isActive
import javax.inject.Inject

private const val ElapsedUpdatePeriodMs = 100L

class PlaybackEngineStateListener @Inject constructor(
    private val metadataMapper: MediaMetadataMapper,
) : Player.Listener, PlaybackEngineState {

    private var player: Player? = null

    private val _playbackStatus = MutableStateFlow(PlaybackStatus.Idle)

    private val _fileMetadata = MutableStateFlow<FileMetadata?>(value = null)

    private val _trackDuration = MutableStateFlow<Long?>(value = null)

    override val playbackStatus: StateFlow<PlaybackStatus> = _playbackStatus.asStateFlow()

    override val fileMetadata: StateFlow<FileMetadata?> = _fileMetadata.asStateFlow()

    override val trackDuration: StateFlow<Long?> = _trackDuration.asStateFlow()

    override val trackElapsed: Flow<Long?> = _playbackStatus
        .flatMapLatest { status ->
            if (status == PlaybackStatus.Playing) {
                createRunningElapsedTimeFlow()
            } else {
                createCurrentElapsedTimeFlow()
            }
        }
        .distinctUntilChanged()

    fun attachPlayer(player: Player) {
        this.player = player
        player.addListener(this)
        initUpdate()
    }

    fun detachPlayer() {
        player?.removeListener(this)
        player = null
    }

    /* Callback implementation */

    override fun onPlaybackStateChanged(playbackState: Int) {
        super.onPlaybackStateChanged(playbackState)
        updatePlaybackState()
    }

    override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
        super.onPlayWhenReadyChanged(playWhenReady, reason)
        updatePlaybackState()
    }

    override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
        super.onMediaMetadataChanged(mediaMetadata)
        updateMetadataState()
    }

    override fun onTimelineChanged(timeline: Timeline, reason: Int) {
        super.onTimelineChanged(timeline, reason)
        updateTrackDuration()
    }

    /* Data fetching routines */

    private fun initUpdate() {
        updateMetadataState()
        updatePlaybackState()
        updateTrackDuration()
    }

    private fun updateMetadataState() {
        val player = player ?: return
        if (player.playbackState == Player.STATE_IDLE) {
            _fileMetadata.tryEmit(null)
            return
        }
        val fileMetadata = metadataMapper.mapToFileMetadata(player.mediaMetadata)
        _fileMetadata.tryEmit(fileMetadata)
    }

    private fun updatePlaybackState() {
        val player = player
        val status = with(player) {
            when {
                this == null -> PlaybackStatus.Idle
                playbackState == Player.STATE_READY && playWhenReady -> PlaybackStatus.Playing
                playbackState == Player.STATE_READY && !playWhenReady -> PlaybackStatus.Paused
                playbackState == Player.STATE_ENDED -> PlaybackStatus.Paused
                playbackState == Player.STATE_BUFFERING -> PlaybackStatus.Loading
                else -> PlaybackStatus.Idle
            }
        }
        _playbackStatus.tryEmit(status)
    }

    private fun updateTrackDuration() {
        val player = player ?: return
        val duration = player.contentDuration.takeIf { it != C.TIME_UNSET } ?: return
        _trackDuration.tryEmit(duration)
    }

    private fun createCurrentElapsedTimeFlow(): Flow<Long?> = flowOf(player?.currentPosition)

    private fun createRunningElapsedTimeFlow(): Flow<Long?> = channelFlow {
        while (isActive) {
            val elapsed = player?.currentPosition
            send(elapsed)
            delay(ElapsedUpdatePeriodMs)
        }
    }
}