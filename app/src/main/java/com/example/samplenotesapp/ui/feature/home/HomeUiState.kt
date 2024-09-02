package com.example.samplenotesapp.ui.feature.home

import com.example.samplenotesapp.domain.model.SortType
import com.example.samplenotesapp.ui.model.NotePresentation

data class HomeUiState(
    val isLoading: Boolean = false,
    val notes: List<NotePresentation> = listOf(),
    val sortType: SortType = SortType.NONE,
) {
}
