package io.alexeychurchill.clown.library.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.alexeychurchill.clown.library.domain.AddDirectoryUseCase
import io.alexeychurchill.clown.library.domain.PathCodec
import io.alexeychurchill.clown.library.presentation.LibraryDirection.Directory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val addFolderUseCase: AddDirectoryUseCase,
    private val pathCodec: PathCodec,
) : ViewModel() {

    private val mutableOpenDirectoryFlow = MutableSharedFlow<Unit>()

    private val mutableDirectionFlow = MutableSharedFlow<LibraryDirection>()

    val openDirectoryFlow: Flow<Unit>
        get() = mutableOpenDirectoryFlow

    val directionFlow: Flow<LibraryDirection>
        get() = mutableDirectionFlow

    fun onAction(action: LibraryAction) {
        viewModelScope.launch {
            when (action) {
                is LibraryAction.OpenMediaEntry -> {
                    if (action.type == MediaEntryItemViewState.Type.Directory) {
                        action.path?.let { path ->
                            val direction = Directory(pathId = pathCodec.encode(path))
                            mutableDirectionFlow.emit(direction)
                        }
                    }
                }

                LibraryAction.OpenTreePicker -> mutableOpenDirectoryFlow.emit(Unit)
                is LibraryAction.TreePicked -> addFolderUseCase(action.uriPath)
            }
        }
    }
}
