@file:OptIn(ExperimentalCoroutinesApi::class)

package io.alexeychurchill.clown.library.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.alexeychurchill.clown.core.viewstate.ViewAction
import io.alexeychurchill.clown.library.domain.AddDirectoryUseCase
import io.alexeychurchill.clown.library.domain.DirectoryRepository
import io.alexeychurchill.clown.library.viewstate.LibraryViewAction
import io.alexeychurchill.clown.library.viewstate.LibraryViewAction.AddFolder
import io.alexeychurchill.clown.library.viewstate.LibraryViewAction.OnFolderPicked
import io.alexeychurchill.clown.library.viewstate.LibraryViewState
import io.alexeychurchill.clown.library.viewstate.LibraryViewStateMapper
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
    directoryRepository: DirectoryRepository,
    private val addFolderUseCase: AddDirectoryUseCase,
    private val libraryViewStateMapper: LibraryViewStateMapper
) : ViewModel() {

    private val mutableOpenDirectoryPickerFlow = MutableSharedFlow<Unit>()

    val libraryViewState: StateFlow<LibraryViewState> = directoryRepository
        .allDirectories
        .mapLatest(libraryViewStateMapper::mapToViewState)
        .flowOn(Dispatchers.IO)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = LibraryViewState.Loading
        )

    val openDirectoryFlow: Flow<Unit>
        get() = mutableOpenDirectoryPickerFlow

    fun onEvent(event: ViewAction) {
        if (event !is LibraryViewAction) return
        viewModelScope.launch { handleEvents(event) }
    }

    private suspend fun handleEvents(event: LibraryViewAction) {
        when (event) {
            AddFolder -> mutableOpenDirectoryPickerFlow.emit(Unit)
            is OnFolderPicked -> addFolderUseCase(event.treeUri)
            else -> {}
        }
    }
}
