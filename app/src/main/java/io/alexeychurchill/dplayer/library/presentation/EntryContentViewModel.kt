package io.alexeychurchill.dplayer.library.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import io.alexeychurchill.dplayer.core.domain.filesystem.FileSystemEntry
import io.alexeychurchill.dplayer.library.domain.FileSystemRepository
import io.alexeychurchill.dplayer.library.presentation.mapper.EntryContentViewStateMapper
import io.alexeychurchill.dplayer.library.presentation.model.EntryContentSectionsViewState
import io.alexeychurchill.dplayer.library.presentation.model.EntryContentViewState
import io.alexeychurchill.dplayer.library.presentation.model.EntryContentViewState.*
import io.alexeychurchill.dplayer.media.domain.FileMetadataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.Eagerly
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

private const val AssistedPayload = "payload"

@HiltViewModel(assistedFactory = EntryContentViewModel.Factory::class)
class EntryContentViewModel @AssistedInject constructor(
    @Assisted(AssistedPayload) private val encodedPayload: String,
    private val fileSystemRepository: FileSystemRepository,
    private val fileMetadataRepository: FileMetadataRepository,
    private val payloadCodec: OpenDirectoryPayloadCodec,
    private val contentMapper: EntryContentViewStateMapper,
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

    val contentState: StateFlow<EntryContentViewState> = createContentState()

    private fun createPayloadState(encodedPayload: String): StateFlow<OpenDirectoryPayload?> {
        val mutableState = MutableStateFlow<OpenDirectoryPayload?>(value = null)
        viewModelScope.launch {
            mutableState.emit(payloadCodec.decode(encodedPayload))
        }
        return mutableState
    }

    private fun createContentState(): StateFlow<EntryContentViewState> {
        val mutableState = MutableStateFlow<EntryContentViewState>(Loading)

        viewModelScope.launch {
            val path = getPath()
            if (path == null) {
                mutableState.emit(Loaded(EntryContentSectionsViewState()))
                return@launch
            }

            val children = fileSystemRepository.getEntriesFor(path)
            val uris = children.mapNotNull { (it.fsEntry as? FileSystemEntry.File)?.path }
            val metadata = fileMetadataRepository.getBatchMetadata(uris)

            val contentState = contentMapper.mapToViewState(children, metadata)
            mutableState.emit(Loaded(contentState))
        }
        return mutableState.asStateFlow()
    }

    private suspend fun getPath(): String? {
        return payloadState
            .filterNotNull()
            .map { payload -> payload.path }
            .firstOrNull()
    }

    @AssistedFactory
    interface Factory {
        fun create(@Assisted(AssistedPayload) payload: String): EntryContentViewModel
    }
}
