package io.alexeychurchill.clown.library.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import io.alexeychurchill.clown.library.domain.FileSystemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn

private const val AssistedPath = "path"

@HiltViewModel(assistedFactory = MediaEntryItemsViewModel.Factory::class)
class MediaEntryItemsViewModel @AssistedInject constructor(
    @Assisted(AssistedPath) private val path: String?,
    private val fileSystemRepository: FileSystemRepository,
    private val mediaEntryMapper: MediaEntryViewStateMapper,
) : ViewModel() {

    val libraryState: StateFlow<LibraryViewState> by lazy {
        flow {
            if (path == null) {
                emit(LibraryViewState.Loaded(emptyList()))
                return@flow
            }

            emit(LibraryViewState.Loading)
            val items = fileSystemRepository
                .getEntriesFor(path)
                .map(mediaEntryMapper::mapToViewState)

            emit(LibraryViewState.Loaded(listOf(LibrarySectionViewState.MediaEntries(items))))
        }
            .flowOn(Dispatchers.IO)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = LibraryViewState.Loading,
            )
    }

    @AssistedFactory
    interface Factory {
        fun create(@Assisted(AssistedPath) path: String?): MediaEntryItemsViewModel
    }
}
