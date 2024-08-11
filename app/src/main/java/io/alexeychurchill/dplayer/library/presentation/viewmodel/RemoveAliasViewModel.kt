package io.alexeychurchill.dplayer.library.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import io.alexeychurchill.dplayer.library.domain.LibraryRepository
import io.alexeychurchill.dplayer.library.presentation.mapper.DirectoryEntryViewStateMapper
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val AssistedDirectoryUri = "directoryUri"

@HiltViewModel(assistedFactory = RemoveAliasViewModel.Factory::class)
class RemoveAliasViewModel @AssistedInject constructor(
    @Assisted(AssistedDirectoryUri) private val directoryUri: String,
    private val libraryRepository: LibraryRepository,
    private val directoryMapper: DirectoryEntryViewStateMapper,
) : ViewModel() {

    private val _onDoneEvent = MutableSharedFlow<Unit>()

    val onDoneEvent = _onDoneEvent.asSharedFlow()

    val aliasName: StateFlow<String?> = createAliasNameState()

    fun confirmRemove() {
        viewModelScope.launch {
            libraryRepository.setDirectoryAlias(directoryUri, aliasName = null)
            _onDoneEvent.emit(Unit)
        }
    }

    private fun createAliasNameState(): StateFlow<String?> {
        val state = MutableStateFlow<String?>(null)

        viewModelScope.launch {
            val entry = libraryRepository.getLibraryEntry(directoryUri) ?: return@launch
            val title = directoryMapper.mapToViewState(entry).visibleTitle
            state.emit(title)
        }
        return state.asStateFlow()
    }

    @AssistedFactory
    interface Factory {
        fun create(@Assisted(AssistedDirectoryUri) directoryUri: String): RemoveAliasViewModel
    }
}
