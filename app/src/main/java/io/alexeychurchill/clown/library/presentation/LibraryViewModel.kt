package io.alexeychurchill.clown.library.presentation

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.alexeychurchill.clown.library.domain.AddDirectoryUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import java.util.Base64
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val addFolderUseCase: AddDirectoryUseCase,
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
                            // TODO: Remove Uri.encode(String) from the view model!
                            mutableDirectionFlow.emit(LibraryDirection.Directory(Uri.encode(path)))
                        }
                    }
                }

                LibraryAction.OpenTreePicker -> mutableOpenDirectoryFlow.emit(Unit)
                is LibraryAction.TreePicked -> addFolderUseCase(action.uriPath)
            }
        }
    }
}
