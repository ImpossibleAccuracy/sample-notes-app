package com.example.samplenotesapp.domain.model

import java.util.Date

data class NoteDomain(
    val id: Long,
    val title: String,
    val description: String?,
    val createdAt: Date,
)
