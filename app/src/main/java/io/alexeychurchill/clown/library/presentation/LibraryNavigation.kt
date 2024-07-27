package io.alexeychurchill.clown.library.presentation

import io.alexeychurchill.clown.core.presentation.Direction

sealed interface LibraryDirection : Direction {

    companion object {
        val start = Root.navPath
    }

    data object Root : LibraryDirection {

        override val navPath: String = "library"
    }

    data class Directory(val path: String) : LibraryDirection {

        companion object {
            const val ArgPath = "directoryPath"
            const val NavPattern = "library?path={$ArgPath}"
        }

        override val navPath: String
            get() = NavPattern.replace("{$ArgPath}", path)
    }
}
