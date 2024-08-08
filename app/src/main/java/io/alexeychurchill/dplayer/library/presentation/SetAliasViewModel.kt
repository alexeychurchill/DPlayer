package io.alexeychurchill.dplayer.library.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
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

    private val _aliasValue = MutableStateFlow("")

    private val _actionsEnabled = MutableStateFlow(true)

    val onDoneEvent: Flow<Unit> = _onDoneEvent.asSharedFlow()

    val aliasValue: StateFlow<String> = _aliasValue.asStateFlow()

    val applyEnabled: StateFlow<Boolean> = _actionsEnabled
        .combine(_aliasValue) { actionsEnabled, aliasValue ->
            actionsEnabled && aliasValue.isNotEmpty() && aliasValue.isNotBlank()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = false,
        )

    val cancelEnabled: StateFlow<Boolean> = _actionsEnabled.asStateFlow()

    fun onAliasValueChange(value: String) {
        _aliasValue.tryEmit(value)
    }

    fun onApply() {
        if (!applyEnabled.value) return
        viewModelScope.launch {
            _actionsEnabled.emit(false)
            val aliasValue = _aliasValue.value.trim()
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
