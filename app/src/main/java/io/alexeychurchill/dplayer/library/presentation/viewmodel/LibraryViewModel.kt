package io.alexeychurchill.dplayer.library.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.alexeychurchill.dplayer.core.domain.MediaId
import io.alexeychurchill.dplayer.media.domain.PlaybackEngine
import io.alexeychurchill.dplayer.library.domain.AddDirectoryUseCase
import io.alexeychurchill.dplayer.library.presentation.mapper.OpenDirectoryPayloadCodec
import io.alexeychurchill.dplayer.library.presentation.model.LibraryAction
import io.alexeychurchill.dplayer.library.presentation.model.LibraryDirection
import io.alexeychurchill.dplayer.library.presentation.model.LibraryDirection.Directory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val addFolderUseCase: AddDirectoryUseCase,
    private val payloadCodec: OpenDirectoryPayloadCodec,
    private val playbackEngine: PlaybackEngine,
) : ViewModel() {

    private val _openDirectoryFlow = MutableSharedFlow<Unit>()

    private val _directionFlow = MutableSharedFlow<LibraryDirection>()

    private val _backDirectionFlow = MutableSharedFlow<Unit>()

    val openDirectoryFlow: Flow<Unit>
        get() = _openDirectoryFlow

    val directionFlow: Flow<LibraryDirection>
        get() = _directionFlow

    val backDirectionFlow: Flow<Unit>
        get() = _backDirectionFlow

    fun onAction(action: LibraryAction) {
        viewModelScope.launch {
            when (action) {
                is LibraryAction.OpenMediaEntry.File -> {
                    playbackEngine.use(MediaId.Local(action.path))
                }

                is LibraryAction.OpenMediaEntry.Directory -> {
                    val encodedPayload = payloadCodec.encode(action.payload)
                    _directionFlow.emit(Directory(encodedPayload = encodedPayload))
                }

                LibraryAction.OpenTreePicker -> _openDirectoryFlow.emit(Unit)

                is LibraryAction.TreePicked -> addFolderUseCase(action.uriPath)

                LibraryAction.GoBack -> _backDirectionFlow.emit(Unit)
            }
        }
    }

    fun openPlaybackIfRequested(shouldOpenPlayback: Boolean) {
        TODO("Not yet implemented")
    }
}
