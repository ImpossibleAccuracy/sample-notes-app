package com.example.samplenotesapp.data.repository

import com.example.samplenotesapp.domain.model.NoteDomain
import com.example.samplenotesapp.domain.repository.NoteRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Date
import kotlin.random.Random

object InMemoryNoteRepository : NoteRepository {
    private val _notes = MutableStateFlow<List<NoteDomain>>(listOf())
    override val notes: Flow<List<NoteDomain>> = _notes.asStateFlow()

    override suspend fun updateNotes() {
        // Capture notes value before clear
        val value = _notes.value

        // Clear notes
        _notes.value = listOf()

        // Add delay to simulate network
        delay(500)

        if (value.isNotEmpty()) {
            _notes.value = value
            return
        }

        // If notes is not added, then add sample notes
        _notes.value = listOf(
            NoteDomain(
                id = Random.nextLong(),
                title = "Play minecraft",
                description = "Lorem ipsum",
                createdAt = Date(),
            ),
            NoteDomain(
                id = Random.nextLong(),
                title = "Take pills",
                description = "Lorem ipsum",
                createdAt = Date(),
            ),
            NoteDomain(
                id = Random.nextLong(),
                title = "IDK",
                description = "Lorem ipsum",
                createdAt = Date(),
            ),
        )
    }
}