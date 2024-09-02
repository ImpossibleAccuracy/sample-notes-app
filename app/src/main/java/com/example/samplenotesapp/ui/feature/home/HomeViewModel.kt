package com.example.samplenotesapp.ui.feature.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.samplenotesapp.data.repository.InMemoryNoteRepository
import com.example.samplenotesapp.domain.model.NoteDomain
import com.example.samplenotesapp.domain.repository.NoteRepository
import com.example.samplenotesapp.ui.mapper.toPresentation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    application: Application,
) : AndroidViewModel(application) {
    private val noteRepository: NoteRepository = InMemoryNoteRepository
    private val dateFormat = android.text.format.DateFormat.getLongDateFormat(application)

    private val _state = MutableStateFlow(HomeUiState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            noteRepository
                .notes
                .collect { notes ->
                    val resultNotes = sortNotes(notes, state.value.sortType)
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

    fun setSortType(type: HomeUiState.SortType) {
        _state.update {
            it.copy(
                sortType = type
            )
        }

        loadNotes()
    }

    private fun sortNotes(
        notes: List<NoteDomain>,
        sortType: HomeUiState.SortType
    ): List<NoteDomain> =
        when (sortType) {
            HomeUiState.SortType.NONE -> notes
            HomeUiState.SortType.TITLE -> notes.sortedBy { it.title }
            HomeUiState.SortType.CREATED_AT -> notes.sortedBy { it.createdAt }
        }
}