package com.example.samplenotesapp.ui.feature.create

data class AddNoteUiState(
    val titleValue: String = "",
    val isTitleInvalid: Boolean = false,
    val descriptionValue: String = "",
    val isDescriptionInvalid: Boolean = false,
)