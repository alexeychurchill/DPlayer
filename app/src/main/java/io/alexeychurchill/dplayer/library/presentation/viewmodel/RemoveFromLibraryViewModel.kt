package io.alexeychurchill.dplayer.library.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import io.alexeychurchill.dplayer.library.domain.LibraryRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

private const val AssistedDirectoryUri = "directoryUri"

@HiltViewModel(assistedFactory = RemoveFromLibraryViewModel.Factory::class)
class RemoveFromLibraryViewModel @AssistedInject constructor(
    @Assisted(AssistedDirectoryUri) private val directoryUri: String,
    private val libraryRepository: LibraryRepository,
) : ViewModel() {

    private val _onDoneEvent = MutableSharedFlow<Unit>()

    val onDoneEvent = _onDoneEvent.asSharedFlow()

    fun confirmRemove() {
        viewModelScope.launch {
            libraryRepository.removeEntry(directoryUri)
            _onDoneEvent.emit(Unit)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted(AssistedDirectoryUri) directoryUri: String,
        ): RemoveFromLibraryViewModel
    }
}
