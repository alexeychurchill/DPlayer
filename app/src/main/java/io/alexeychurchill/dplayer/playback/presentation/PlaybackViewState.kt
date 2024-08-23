package io.alexeychurchill.dplayer.playback.presentation

data class PlaybackFlowViewState(
    val controlsEnabled: Boolean,
    val playbackState: PlaybackState,
)

enum class PlaybackState {
    Unknown,
    Playing,
    Paused,
}

data class PlayingTrackInfoViewState(
    val title: String? = null,
    val artist: String? = null,
)

sealed interface PlaybackAction {

    data object TogglePlayback : PlaybackAction

    data object Rewind : PlaybackAction

    data object Next : PlaybackAction

    data object FastRewindStart : PlaybackAction

    data object FastRewindStop : PlaybackAction

    data object FastForwardStart : PlaybackAction

    data object FastForwardStop : PlaybackAction
}
