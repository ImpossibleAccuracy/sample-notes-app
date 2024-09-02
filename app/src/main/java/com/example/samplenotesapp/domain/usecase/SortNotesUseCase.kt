package com.example.samplenotesapp.domain.usecase

import com.example.samplenotesapp.domain.model.NoteDomain
import com.example.samplenotesapp.domain.model.SortType

class SortNotesUseCase {
    operator fun invoke(list: List<NoteDomain>, sortType: SortType) = when (sortType) {
        SortType.NONE -> list
        SortType.TITLE -> list.sortedBy { it.title }
        SortType.CREATED_AT -> list.sortedBy { it.createdAt }
    }
}