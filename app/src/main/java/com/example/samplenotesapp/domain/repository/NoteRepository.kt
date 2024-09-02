package com.example.samplenotesapp.domain.repository

import com.example.samplenotesapp.domain.model.NoteDomain
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    val notes: Flow<List<NoteDomain>>

    suspend fun updateNotes()

    suspend fun saveNote(
        title: String,
        description: String,
    ): Result<Unit>
}