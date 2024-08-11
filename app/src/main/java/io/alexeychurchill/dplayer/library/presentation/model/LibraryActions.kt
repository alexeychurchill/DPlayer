package io.alexeychurchill.dplayer.library.presentation.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

typealias OnLibraryAction = (LibraryAction) -> Unit

sealed interface LibraryAction {

    data object OpenTreePicker : LibraryAction

    data class TreePicked(val uriPath: String?) : LibraryAction

    sealed interface OpenMediaEntry : LibraryAction {

        data class File(
            val path: String,
        ) : OpenMediaEntry

        data class Directory(
            val payload: OpenDirectoryPayload,
        ) : OpenMediaEntry
    }

    data object GoBack : LibraryAction
}

/**
 * Describes some data of the directory which is being opened.
 *
 * For the sake of simplicity, it's directly JSON serializable.
 * Moreover, the "standard" kotlinx JSON serde library is used,
 * so not a very big problem.
 */
@Serializable
data class OpenDirectoryPayload(

    @SerialName("path")
    val path: String,

    @SerialName("title")
    val title: String,
)
