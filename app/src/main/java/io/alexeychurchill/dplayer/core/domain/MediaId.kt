package io.alexeychurchill.dplayer.core.domain

sealed interface MediaId {

    data class Local(val uri: String) : MediaId
}
