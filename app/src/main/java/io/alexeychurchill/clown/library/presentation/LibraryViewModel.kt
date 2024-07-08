@file:OptIn(ExperimentalCoroutinesApi::class)

package io.alexeychurchill.clown.library.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.alexeychurchill.clown.library.domain.AddDirectoryUseCase
import io.alexeychurchill.clown.library.domain.LibraryEntry
import io.alexeychurchill.clown.library.domain.LibraryRepository
import io.alexeychurchill.clown.library.viewstate.DirectoryLibraryListItemStateMapper
import io.alexeychurchill.clown.library.viewstate.LibraryViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    libraryRepository: LibraryRepository,
    private val addFolderUseCase: AddDirectoryUseCase,
    private val directoryItemStateMapper: DirectoryLibraryListItemStateMapper,
) : ViewModel() {

    private val mutableOpenDirectoryPickerFlow = MutableSharedFlow<Unit>()

    val libraryViewState: StateFlow<LibraryViewState> = libraryRepository
        .allEntries
        .mapLatest {
            val dirEntries = it.filterIsInstance<LibraryEntry.Directory>()
            LibraryViewState.Loaded(dirEntries.map(directoryItemStateMapper::mapToListItemState))
        }
        .flowOn(Dispatchers.IO)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = LibraryViewState.Loading
        )

    val openDirectoryFlow: Flow<Unit>
        get() = mutableOpenDirectoryPickerFlow

    fun onAddTap() {
        viewModelScope.launch {
            mutableOpenDirectoryPickerFlow.emit(Unit)
        }
    }

    fun onAddUri(uri: String?) {
        viewModelScope.launch {
            addFolderUseCase(uri)
        }
    }
}
