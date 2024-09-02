package com.example.samplenotesapp.ui.feature.create

import androidx.annotation.StringRes

sealed interface AddNoteUiAction {
    data object NavigateSuccess : AddNoteUiAction

    data class ShowError(
        @StringRes
        val message: Int
    ) : AddNoteUiAction
}