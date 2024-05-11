package io.alexeychurchill.clown.library.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.alexeychurchill.clown.core.viewstate.ViewAction
import io.alexeychurchill.clown.library.domain.AddDirectoryUseCase
import io.alexeychurchill.clown.library.viewstate.LibraryViewAction
import io.alexeychurchill.clown.library.viewstate.LibraryViewAction.AddFolder
import io.alexeychurchill.clown.library.viewstate.LibraryViewAction.OnFolderPicked
import io.alexeychurchill.clown.library.viewstate.LibraryViewState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val addFolderUseCase: AddDirectoryUseCase,
) : ViewModel() {

    private val mutableLibraryViewState: MutableStateFlow<LibraryViewState> =
        MutableStateFlow(LibraryViewState.Loading)

    private val mutableOpenDirectoryPickerFlow = MutableSharedFlow<Unit>()

    val libraryViewState: StateFlow<LibraryViewState>
        get() = mutableLibraryViewState

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
