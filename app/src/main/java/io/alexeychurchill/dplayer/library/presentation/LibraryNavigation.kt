package io.alexeychurchill.dplayer.library.presentation

import io.alexeychurchill.dplayer.core.presentation.Direction

sealed interface LibraryDirection : Direction {

    companion object {
        val start = Root.navPath
    }

    data object Root : LibraryDirection {

        override val navPath: String = "library"
    }

    data class Directory(val encodedPayload: String) : LibraryDirection {

        companion object {
            const val ArgPayload = "payload"
            const val NavPattern = "library?payload={$ArgPayload}"
        }

        override val navPath: String
            get() = NavPattern.replace("{$ArgPayload}", encodedPayload)
    }
}
