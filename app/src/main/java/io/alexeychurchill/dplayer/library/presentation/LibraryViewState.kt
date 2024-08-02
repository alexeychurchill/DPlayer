package io.alexeychurchill.dplayer.library.presentation

import io.alexeychurchill.dplayer.media.presentation.CoverArtPath


sealed interface LibraryViewState {

    data object Loading : LibraryViewState

    data class Loaded(val sections: List<LibrarySectionViewState>) : LibraryViewState
}

/**
 * Represents any section, such as media entries group, headers, messages etc.
 * in the media library
 */
sealed interface LibrarySectionViewState {

    /**
     * Common parent for headers
     */
    sealed interface Header : LibrarySectionViewState {

        data object ForDirectories : Header

        data object ForFiles : Header
    }

    /**
     * Message for indicating that music files are absent in a media entry
     */
    data object FilesAbsent : LibrarySectionViewState

    /**
     * [MediaEntryItemViewState] (representation of the MediaEntry) collection
     */
    data class MediaEntries(val items: List<MediaEntryItemViewState>) : LibrarySectionViewState
}

data class MediaEntryItemViewState(
    val path: String,
    val title: String,
    val type: Type,
    val status: Status = Status.None,
    val directoryChildInfo: DirectoryChildInfoViewState? = null,
    val fileExtension: String? = null,
    val coverArtPath: CoverArtPath? = null,
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
