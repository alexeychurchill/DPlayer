@file:OptIn(ExperimentalCoroutinesApi::class)

package io.alexeychurchill.dplayer.library.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.alexeychurchill.dplayer.library.domain.LibraryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class LibraryItemsViewModel @Inject constructor(
    libraryRepository: LibraryRepository,
    private val mediaEntryMapper: MediaEntryViewStateMapper,
) : ViewModel() {

    val libraryViewState: StateFlow<LibraryViewState> = libraryRepository
        .allEntries
        .mapLatest {
            LibraryViewState.Loaded(
                sections = listOf(
                    LibrarySectionViewState.MediaEntries(
                        items = it.map(mediaEntryMapper::mapToViewState),
                    ),
                ),
            )
        }
        .flowOn(Dispatchers.IO)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = LibraryViewState.Loading
        )
}
