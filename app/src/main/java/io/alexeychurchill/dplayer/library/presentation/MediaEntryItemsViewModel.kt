package io.alexeychurchill.dplayer.library.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import io.alexeychurchill.dplayer.library.domain.FileSystemRepository
import io.alexeychurchill.dplayer.library.domain.LibraryRepository
import io.alexeychurchill.dplayer.library.domain.PathCodec
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted.Companion.Lazily
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn

private const val AssistedPathId = "pathId"

@HiltViewModel(assistedFactory = MediaEntryItemsViewModel.Factory::class)
class MediaEntryItemsViewModel @AssistedInject constructor(
    @Assisted(AssistedPathId) private val pathId: String?,
    private val libraryRepository: LibraryRepository,
    private val fileSystemRepository: FileSystemRepository,
    private val mediaEntryMapper: MediaEntryViewStateMapper,
    private val titleMapper: MediaEntryTitleMapper,
    private val pathCodec: PathCodec,
) : ViewModel() {

    val titleState: StateFlow<String?> by lazy {
        flow {
            val path = pathId?.let(pathCodec::decode) ?: return@flow
            val mediaEntry = libraryRepository.getLibraryEntry(path)
                ?: fileSystemRepository.getEntryBy(path)

            emit(titleMapper.mapToTitle(mediaEntry))
        }
            .flowOn(Dispatchers.IO)
            .stateIn(
                scope = viewModelScope,
                started = Lazily,
                initialValue = null,
            )
    }

    val libraryState: StateFlow<LibraryViewState> by lazy {
        flow {
            if (pathId == null) {
                emit(LibraryViewState.Loaded(emptyList()))
                return@flow
            }

            emit(LibraryViewState.Loading)
            val path = pathCodec.decode(pathId)
            val items = fileSystemRepository
                .getEntriesFor(path)
                .map(mediaEntryMapper::mapToViewState)

            emit(LibraryViewState.Loaded(listOf(LibrarySectionViewState.MediaEntries(items))))
        }
            .flowOn(Dispatchers.IO)
            .stateIn(
                scope = viewModelScope,
                started = Lazily,
                initialValue = LibraryViewState.Loading,
            )
    }

    @AssistedFactory
    interface Factory {
        fun create(@Assisted(AssistedPathId) pathId: String?): MediaEntryItemsViewModel
    }
}
