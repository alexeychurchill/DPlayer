package io.alexeychurchill.dplayer.library.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import io.alexeychurchill.dplayer.core.domain.filesystem.FileSystemEntry
import io.alexeychurchill.dplayer.library.domain.FileSystemRepository
import io.alexeychurchill.dplayer.library.domain.LibraryRepository
import io.alexeychurchill.dplayer.library.domain.PathCodec
import io.alexeychurchill.dplayer.library.presentation.LibraryViewState.Loaded
import io.alexeychurchill.dplayer.media.domain.FileMetadataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.Lazily
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

private const val AssistedPathId = "pathId"

@HiltViewModel(assistedFactory = MediaEntryContentViewModel.Factory::class)
class MediaEntryContentViewModel @AssistedInject constructor(
    @Assisted(AssistedPathId) private val pathId: String?,
    private val libraryRepository: LibraryRepository,
    private val fileSystemRepository: FileSystemRepository,
    private val fileMetadataRepository: FileMetadataRepository,
    private val titleMapper: MediaEntryTitleMapper,
    private val contentBuilder: AggregateSectionsBuilder,
    private val pathCodec: PathCodec,
) : ViewModel() {

    val titleState: StateFlow<String?> by lazy {
        flow {
            val path = pathId?.let(pathCodec::decode) ?: return@flow
            val mediaEntry = libraryRepository.getLibraryEntry(path)
                ?: fileSystemRepository.getEntryBy(path)

            emit(titleMapper.mapToTitle(mediaEntry, metadata = null))
        }
            .flowOn(Dispatchers.IO)
            .stateIn(
                scope = viewModelScope,
                started = Lazily,
                initialValue = null,
            )
    }

    val libraryState: StateFlow<LibraryViewState> = createLibraryState()

    private fun createLibraryState(): StateFlow<LibraryViewState> {
        val mutableState = MutableStateFlow<LibraryViewState>(LibraryViewState.Loading)

        viewModelScope.launch {
            val path = pathId?.let(pathCodec::decode)
            if (path == null) {
                mutableState.emit(Loaded(emptyList()))
                return@launch
            }

            val mediaEntries = fileSystemRepository.getEntriesFor(path)
            val content = contentBuilder.build(mediaEntries)
            mutableState.emit(Loaded(content))

            val uris = mediaEntries.mapNotNull { (it.fsEntry as? FileSystemEntry.File)?.path }
            val metadata = fileMetadataRepository.getBatchMetadata(uris)
            mutableState.emit(Loaded(contentBuilder.build(mediaEntries, metadata)))
        }

        return mutableState
    }

    @AssistedFactory
    interface Factory {
        fun create(@Assisted(AssistedPathId) pathId: String?): MediaEntryContentViewModel
    }
}
