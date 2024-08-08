@file:OptIn(ExperimentalCoroutinesApi::class)

package io.alexeychurchill.dplayer.library.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.alexeychurchill.dplayer.library.domain.LibraryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibraryContentViewModel @Inject constructor(
    libraryRepository: LibraryRepository,
    private val mediaEntryMapper: MediaEntryViewStateMapper,
) : ViewModel() {

    private val mutableSetAliasState = MutableStateFlow<SetAliasState>(SetAliasState.None)

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

    val setAliasState: StateFlow<SetAliasState> = mutableSetAliasState.asStateFlow()

    fun onSetAliasRequest(directoryUri: String) {
        viewModelScope.launch {
            mutableSetAliasState.emit(SetAliasState.Editing(directoryUri))
        }
    }

    fun onSetAliasDismiss() {
        viewModelScope.launch {
            mutableSetAliasState.emit(SetAliasState.None)
        }
    }
}

sealed interface SetAliasState {

    data object None : SetAliasState

    data class Editing(val directoryUri: String) : SetAliasState
}
