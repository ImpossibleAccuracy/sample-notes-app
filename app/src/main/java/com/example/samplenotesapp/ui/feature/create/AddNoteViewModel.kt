package com.example.samplenotesapp.ui.feature.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.samplenotesapp.R
import com.example.samplenotesapp.data.repository.InMemoryNoteRepository
import com.example.samplenotesapp.domain.repository.NoteRepository
import com.example.samplenotesapp.domain.usecase.ValidateNoteUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddNoteViewModel : ViewModel() {
    private val validateNoteUseCase = ValidateNoteUseCase()
    private val noteRepository: NoteRepository = InMemoryNoteRepository

    private val _state = MutableStateFlow(AddNoteUiState())
    val state = _state.asStateFlow()

    private val _actions = Channel<AddNoteUiAction>(Channel.BUFFERED)
    val actions = _actions.receiveAsFlow()

    fun updateTitle(value: String) {
        _state.update {
            it.copy(
                titleValue = value
            )
        }
    }

    fun updateDescription(value: String) {
        _state.update {
            it.copy(
                descriptionValue = value
            )
        }
    }

    fun saveNote() {
        viewModelScope.launch {
            if (validateData()) {
                noteRepository
                    .saveNote(
                        state.value.titleValue,
                        state.value.descriptionValue,
                    )
                    .onSuccess {
                        _actions.send(AddNoteUiAction.NavigateSuccess)
                    }
                    .onFailure {
                        _actions.send(AddNoteUiAction.ShowError(R.string.note_creation_error))
                    }
            }
        }
    }

    private fun validateData(): Boolean {
        _state.update {
            it.copy(isTitleInvalid = false, isDescriptionInvalid = false)
        }

        val validationErrors =
            validateNoteUseCase(state.value.titleValue, state.value.descriptionValue)

        validationErrors.forEach { error ->
            when (error) {
                ValidateNoteUseCase.ValidationError.TITLE_SHORT,
                ValidateNoteUseCase.ValidationError.TITLE_LONG -> {
                    _state.update {
                        it.copy(isTitleInvalid = true)
                    }
                }

                ValidateNoteUseCase.ValidationError.DESCRIPTION_SHORT,
                ValidateNoteUseCase.ValidationError.DESCRIPTION_LONG -> {
                    _state.update {
                        it.copy(isDescriptionInvalid = true)
                    }
                }
            }
        }

        return validationErrors.isEmpty()
    }
}