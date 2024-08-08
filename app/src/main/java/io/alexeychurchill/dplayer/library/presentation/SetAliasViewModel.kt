package io.alexeychurchill.dplayer.library.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import io.alexeychurchill.dplayer.library.domain.EntrySource
import io.alexeychurchill.dplayer.library.domain.LibraryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

private const val AssistedDirectoryUri = "directoryUri"

@HiltViewModel(assistedFactory = SetAliasViewModel.Factory::class)
class SetAliasViewModel @AssistedInject constructor(
    @Assisted(AssistedDirectoryUri) private val directoryUri: String,
    private val libraryRepository: LibraryRepository,
) : ViewModel() {

    private val _onDoneEvent = MutableSharedFlow<Unit>()

    private val _mode = MutableStateFlow<SetAliasNameMode?>(null)

    private val _aliasValue = MutableStateFlow<String?>(null)

    private val _actionsEnabled = MutableStateFlow(false)

    val onDoneEvent: Flow<Unit> = _onDoneEvent.asSharedFlow()

    val mode: StateFlow<SetAliasNameMode?> = _mode.asStateFlow()

    val aliasValue: StateFlow<String?> = _aliasValue.asStateFlow()

    val applyEnabled: StateFlow<Boolean> = _actionsEnabled
        .combine(_aliasValue) { enabled, aliasValue ->
            val valuePresent = aliasValue?.let { it.isNotEmpty() && it.isNotBlank() } ?: false
            enabled && valuePresent
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = false,
        )

    val cancelEnabled: StateFlow<Boolean> = _actionsEnabled.asStateFlow()

    init {
        viewModelScope.launch {
            val libraryEntry = libraryRepository.getLibraryEntry(directoryUri)
            if (libraryEntry == null || libraryEntry.source !is EntrySource.UserLibrary) {
                _onDoneEvent.emit(Unit)
                return@launch
            }

            val alias = libraryEntry.source.aliasTitle
            if (alias == null) {
                _mode.emit(SetAliasNameMode.Set)
                _aliasValue.emit("")
            } else {
                _mode.emit(SetAliasNameMode.Update)
                _aliasValue.emit(alias)
            }
            _actionsEnabled.emit(true)
        }
    }

    fun onAliasValueChange(value: String) {
        _aliasValue.tryEmit(value)
    }

    fun onApply() {
        if (!applyEnabled.value) return
        viewModelScope.launch {
            val aliasValue = _aliasValue.value?.trim() ?: return@launch
            _actionsEnabled.emit(false)
            libraryRepository.setDirectoryAlias(directoryUri, aliasValue)
            _onDoneEvent.emit(Unit)
        }
    }

    fun onCancel() {
        if (!cancelEnabled.value) return
        viewModelScope.launch {
            _actionsEnabled.emit(false)
            _onDoneEvent.emit(Unit)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(@Assisted(AssistedDirectoryUri) directoryUri: String): SetAliasViewModel
    }
}

enum class SetAliasNameMode {
    Set,
    Update,
}
