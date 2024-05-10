package io.alexeychurchill.clown.library.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.alexeychurchill.clown.core.ui.viewstate.ViewAction
import io.alexeychurchill.clown.library.viewstate.LibraryViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor() : ViewModel() {

    private val mutableLibraryViewState: MutableStateFlow<LibraryViewState> =
        MutableStateFlow(LibraryViewState.Loading)

    val libraryViewState: StateFlow<LibraryViewState>
        get() = mutableLibraryViewState

    fun onEvent(event: ViewAction) {
        // TODO: Handle library view actions
    }
}
