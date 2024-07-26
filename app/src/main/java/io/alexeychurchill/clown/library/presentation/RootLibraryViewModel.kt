@file:OptIn(ExperimentalCoroutinesApi::class)

package io.alexeychurchill.clown.library.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.alexeychurchill.clown.library.domain.AddDirectoryUseCase
import io.alexeychurchill.clown.library.domain.LibraryRepository
import io.alexeychurchill.clown.library.domain.MediaEntry
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
class RootLibraryViewModel @Inject constructor(
    libraryRepository: LibraryRepository,
    private val addFolderUseCase: AddDirectoryUseCase,
    private val mediaEntryMapper: MediaEntryViewStateMapper,
) : ViewModel() {

    private val mutableOpenDirectoryPickerFlow = MutableSharedFlow<Unit>()

    val libraryViewState: StateFlow<LibraryViewState> = libraryRepository
        .allEntries
        .mapLatest {
            val directoryEntries = it.filterIsInstance<MediaEntry.Directory>()
            val items = directoryEntries.map(mediaEntryMapper::mapToViewState)
            LibraryViewState.Loaded(
                sections = listOf(
                    LibrarySectionViewState.MediaEntries(items = items),
                ),
            )
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

    fun onItemTap(item: LibrarySectionViewState) {
        // TODO: Handle item -> navigate
    }
}
