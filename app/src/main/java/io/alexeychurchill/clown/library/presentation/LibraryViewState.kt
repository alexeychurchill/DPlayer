package io.alexeychurchill.clown.library.presentation


sealed interface LibraryViewState {

    data object Loading : LibraryViewState

    data class Loaded(val sections: List<LibrarySectionViewState>) : LibraryViewState
}

sealed interface LibrarySectionViewState {

    // TODO: Add sections headers
    /* sealed interface Header : LibrarySectionViewState */

    data class MediaEntries(val items: List<MediaEntryItemViewState>) : LibrarySectionViewState
}

data class MediaEntryItemViewState(
    val path: String?,
    val title: String,
    val type: Type,
    val status: Status = Status.None,
    val directoryChildInfo: DirectoryChildInfoViewState? = null,
) {

    enum class Type {
        None,
        MusicFile,
        Directory,
    }

    enum class Status {
        None,
        Openable,
        Faulty,
    }
}

data class DirectoryChildInfoViewState(
    val fileCount: Int?,
    val subDirectoryCount: Int?,
)
