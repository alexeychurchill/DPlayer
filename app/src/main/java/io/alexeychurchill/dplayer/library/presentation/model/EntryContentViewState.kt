package io.alexeychurchill.dplayer.library.presentation.model

sealed interface EntryContentViewState {

    data object Loading : EntryContentViewState

    data class Loaded(
        val sectionsState: EntryContentSectionsViewState,
    ) : EntryContentViewState
}

data class EntryContentSectionsViewState(
    val directorySectionHeaderPresent: Boolean = false,
    val directoryEntries: List<DirectoryEntryViewState> = emptyList(),
    val filesSectionHeaderPresent: Boolean = false,
    val fileEntries: List<FileEntryViewState> = emptyList(),
    val noFilesHeaderPresent: Boolean = false,
)
