@file:OptIn(ExperimentalCoroutinesApi::class)

package io.alexeychurchill.dplayer.library.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.alexeychurchill.dplayer.library.domain.LibraryRepository
import io.alexeychurchill.dplayer.library.presentation.mapper.LibraryEntryViewStateMapper
import io.alexeychurchill.dplayer.library.presentation.model.LibraryDirectoryAction
import io.alexeychurchill.dplayer.library.presentation.model.LibraryRootViewState
import io.alexeychurchill.dplayer.library.presentation.model.LibraryRootViewState.Loaded
import io.alexeychurchill.dplayer.library.presentation.model.LibraryRootViewState.Loading
import io.alexeychurchill.dplayer.library.presentation.model.SetAliasViewState
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
class LibraryRootViewModel @Inject constructor(
    libraryRepository: LibraryRepository,
    private val entryMapper: LibraryEntryViewStateMapper,
) : ViewModel() {

    private val mutableSetAliasState = MutableStateFlow<SetAliasViewState>(SetAliasViewState.None)

    val libraryViewState: StateFlow<LibraryRootViewState> = libraryRepository
        .allEntries
        .mapLatest { mediaEntries ->
            Loaded(
                items = mediaEntries.map(entryMapper::mapToViewState),
            )
        }
        .flowOn(Dispatchers.IO)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = Loading,
        )

    val setAliasState: StateFlow<SetAliasViewState> = mutableSetAliasState.asStateFlow()

    fun onDirectoryAction(action: LibraryDirectoryAction) {
        viewModelScope.launch {
            when (action) {
                is LibraryDirectoryAction.SetAlias, is LibraryDirectoryAction.UpdateAlias -> {
                    mutableSetAliasState.emit(SetAliasViewState.Editing(action.path))
                }

                is LibraryDirectoryAction.RemoveAlias -> {
                    TODO()
                }
            }
        }
    }

    fun onSetAliasDismiss() {
        viewModelScope.launch {
            mutableSetAliasState.emit(SetAliasViewState.None)
        }
    }
}
