package com.example.samplenotesapp.ui.feature.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.samplenotesapp.data.repository.InMemoryNoteRepository
import com.example.samplenotesapp.domain.model.SortType
import com.example.samplenotesapp.domain.repository.NoteRepository
import com.example.samplenotesapp.domain.usecase.SortNotesUseCase
import com.example.samplenotesapp.ui.mapper.toPresentation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    application: Application,
) : AndroidViewModel(application) {
    private val sortNotesUseCase = SortNotesUseCase()
    private val noteRepository: NoteRepository = InMemoryNoteRepository

    private val dateFormat = android.text.format.DateFormat.getLongDateFormat(application)

    private val _state = MutableStateFlow(HomeUiState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            noteRepository
                .notes
                .collect { notes ->
                    val resultNotes = sortNotesUseCase(notes, state.value.sortType)
                        .map { note ->
                            note.toPresentation(dateFormat)
                        }

                    _state.update {
                        it.copy(
                            notes = resultNotes,
                        )
                    }
                }
        }

        loadNotes()
    }

    fun loadNotes() {
        // No need to load notes if loading already running
        if (state.value.isLoading) return

        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true)
            }

            noteRepository.updateNotes()

            _state.update {
                it.copy(isLoading = false)
            }
        }
    }

    fun setSortType(type: SortType) {
        _state.update {
            it.copy(
                sortType = type
            )
        }

        loadNotes()
    }
}