package io.alexeychurchill.clown.library.viewstate

import io.alexeychurchill.clown.core.viewstate.ViewAction

sealed interface LibraryListItemState {

    sealed interface Header : LibraryListItemState

    data class Entry(
        val title: String,
        val type: Type,
        val status: Status = Status.None,
        val metaItems: List<LibraryListItemMeta> = emptyList(),
        val onTap: ViewAction = ViewAction.noop(),
    ) : LibraryListItemState {

        sealed interface Type {

            data object None : Type

            data object MusicFile : Type

            data object Directory : Type
        }

        enum class Status {
            None,
            Openable,
            Faulty,
        }
    }
}

sealed interface LibraryListItemMeta {

    data class DirectoryCount(val count: Int) : LibraryListItemMeta

    data class MusicFileCount(val count: Int) : LibraryListItemMeta
}
