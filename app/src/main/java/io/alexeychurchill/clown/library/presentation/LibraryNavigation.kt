package io.alexeychurchill.clown.library.presentation

import io.alexeychurchill.clown.core.presentation.Direction

sealed interface LibraryDirection : Direction {

    companion object {
        val start = Root.navPath
    }

    data object Root : LibraryDirection {

        override val navPath: String = "library"
    }

    data class Directory(val pathId: String) : LibraryDirection {

        companion object {
            const val ArgPathId = "directoryPathId"
            const val NavPattern = "library?path={$ArgPathId}"
        }

        override val navPath: String
            get() = NavPattern.replace("{$ArgPathId}", pathId)
    }
}
