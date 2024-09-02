package com.example.samplenotesapp.ui.mapper

import com.example.samplenotesapp.domain.model.NoteDomain
import com.example.samplenotesapp.ui.model.NotePresentation
import java.text.DateFormat

fun NoteDomain.toPresentation(dateFormat: DateFormat) = NotePresentation(
    id = id,
    title = title,
    description = description,
    createdAt = dateFormat.format(createdAt)
)
