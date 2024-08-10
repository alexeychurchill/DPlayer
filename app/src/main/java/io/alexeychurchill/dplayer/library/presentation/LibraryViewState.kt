package io.alexeychurchill.dplayer.library.presentation

import io.alexeychurchill.dplayer.library.presentation.viewstate.FileEntryViewState
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

    /**
     * Files section
     */
    data class FilesSection(
        val items: List<FileEntryViewState>,
    ) : LibrarySectionViewState
}

@Deprecated(message = "Should be split into particular type view states")
data class MediaEntryItemViewState(
    val path: String,
    val title: String,
    val type: Type,
    val artist: String? = null,
    val status: Status = Status.None,
    val secondaryInfo: SecondaryInfoViewState? = null,
    val fileExtension: String? = null,
    val coverArtPath: CoverArtPath? = null,
    val openAction: LibraryAction,
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

sealed interface SecondaryInfoViewState {

    data class DirectoryChildInfo(
        val fileCount: Int?,
        val subDirectoryCount: Int?,
    ) : SecondaryInfoViewState

    data class TrackInfo(
        val artist: String? = null,
        val year: Int? = null,
    ) : SecondaryInfoViewState
}
