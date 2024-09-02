package com.example.samplenotesapp.domain.usecase

private const val MIN_TITLE_LENGTH = 3
private const val MAX_TITLE_LENGTH = 100

private const val MIN_DESCRIPTION_LENGTH = 3
private const val MAX_DESCRIPTION_LENGTH = 1000

class ValidateNoteUseCase {
    operator fun invoke(title: String, description: String): List<ValidationError> {
        val trimTitle = title.trim()
        val trimDescription = description.trim()

        val result = mutableListOf<ValidationError>()

        if (trimTitle.length < MIN_TITLE_LENGTH) {
            result.add(ValidationError.TITLE_SHORT)
        } else if (trimTitle.length > MAX_TITLE_LENGTH) {
            result.add(ValidationError.TITLE_LONG)
        }

        if (trimDescription.length < MIN_DESCRIPTION_LENGTH) {
            result.add(ValidationError.DESCRIPTION_SHORT)
        } else if (trimDescription.length > MAX_DESCRIPTION_LENGTH) {
            result.add(ValidationError.DESCRIPTION_LONG)
        }

        return result
    }

    enum class ValidationError {
        TITLE_SHORT,
        TITLE_LONG,
        DESCRIPTION_SHORT,
        DESCRIPTION_LONG,
    }
}