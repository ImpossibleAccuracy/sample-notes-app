package com.example.samplenotesapp.ui.feature.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.samplenotesapp.data.repository.InMemoryNoteRepository
import com.example.samplenotesapp.domain.repository.NoteRepository
import com.example.samplenotesapp.ui.mapper.toPresentation
import com.example.samplenotesapp.ui.model.NotePresentation
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class NoteDetailsViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle,
) : AndroidViewModel(application) {
    private val noteRepository: NoteRepository = InMemoryNoteRepository
    private val id: Long = savedStateHandle["id"]!!

    private val dateFormat = android.text.format.DateFormat.getLongDateFormat(application)

    val note: StateFlow<NotePresentation?> = noteRepository
        .observeNote(id)
        .map {
            it?.toPresentation(dateFormat)
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )
}