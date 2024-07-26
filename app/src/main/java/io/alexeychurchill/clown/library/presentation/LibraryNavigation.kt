package io.alexeychurchill.clown.library.presentation

sealed interface LibraryDirection {

    data class Directory(val path: String) : LibraryDirection
}
