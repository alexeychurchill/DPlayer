package io.alexeychurchill.dplayer.library.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import io.alexeychurchill.dplayer.core.domain.filesystem.FileSystemEntry
import io.alexeychurchill.dplayer.library.domain.FileSystemRepository
import io.alexeychurchill.dplayer.library.presentation.LibraryViewState.Loaded
import io.alexeychurchill.dplayer.media.domain.FileMetadataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.Eagerly
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

private const val AssistedPayload = "payload"

@HiltViewModel(assistedFactory = MediaEntryContentViewModel.Factory::class)
class MediaEntryContentViewModel @AssistedInject constructor(
    @Assisted(AssistedPayload) private val encodedPayload: String,
    private val fileSystemRepository: FileSystemRepository,
    private val fileMetadataRepository: FileMetadataRepository,
    private val contentBuilder: AggregateSectionsBuilder,
    private val payloadCodec: OpenDirectoryPayloadCodec,
) : ViewModel() {

    private val payloadState = createPayloadState(encodedPayload)

    val titleState: StateFlow<String?> = payloadState
        .filterNotNull()
        .map { payload -> payload.title }
        .stateIn(
            scope = viewModelScope,
            started = Eagerly,
            initialValue = null,
        )

    val libraryState: StateFlow<LibraryViewState> = createLibraryState()

    private fun createPayloadState(encodedPayload: String): StateFlow<OpenDirectoryPayload?> {
        val mutableState = MutableStateFlow<OpenDirectoryPayload?>(value = null)
        viewModelScope.launch {
            mutableState.emit(payloadCodec.decode(encodedPayload))
        }
        return mutableState
    }

    private fun createLibraryState(): StateFlow<LibraryViewState> {
        val mutableState = MutableStateFlow<LibraryViewState>(LibraryViewState.Loading)

        viewModelScope.launch {
            val path = payloadState
                .filterNotNull()
                .map { payload -> payload.path }
                .firstOrNull()

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
        fun create(@Assisted(AssistedPayload) payload: String): MediaEntryContentViewModel
    }
}
