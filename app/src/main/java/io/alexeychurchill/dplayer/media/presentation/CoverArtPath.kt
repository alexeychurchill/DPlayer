package io.alexeychurchill.dplayer.media.presentation


sealed interface CoverArtPath {

    data class LocalUri(val mediaUri: String) : CoverArtPath
}
