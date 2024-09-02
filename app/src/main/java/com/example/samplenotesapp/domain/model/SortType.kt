package com.example.samplenotesapp.domain.model

import androidx.annotation.StringRes
import com.example.samplenotesapp.R

enum class SortType(
    @StringRes
    val title: Int,
) {
    NONE(title = R.string.no_sort),
    TITLE(title = R.string.sort_by_title),
    CREATED_AT(title = R.string.sort_by_date),
}