package com.example.samplenotesapp.ui.feature.home

import androidx.annotation.StringRes
import com.example.samplenotesapp.R
import com.example.samplenotesapp.ui.model.NotePresentation

data class HomeUiState(
    val isLoading: Boolean = false,
    val notes: List<NotePresentation> = listOf(),
    val sortType: SortType = SortType.NONE,
) {

    enum class SortType(
        @StringRes
        val title: Int,
    ) {
        NONE(title = R.string.no_sort),
        TITLE(title = R.string.sort_by_title),
        CREATED_AT(title = R.string.sort_by_date),
    }
}
