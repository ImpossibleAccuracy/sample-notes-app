package com.example.samplenotesapp.ui.model

data class NotePresentation(
    val id: Long,
    val title: String,
    val description: String?,
    val createdAt: String,
)