package com.example.prepdeck.domain.model

import androidx.compose.ui.graphics.Color
import com.example.prepdeck.ui.theme.gradient1
import com.example.prepdeck.ui.theme.gradient2
import com.example.prepdeck.ui.theme.gradient3
import com.example.prepdeck.ui.theme.gradient4
import com.example.prepdeck.ui.theme.gradient5

data class Subject(
    val name: String,
    val goalHourse: Float,
    val colors: List<Color>,
    val subjectId: Int
) {
    companion object{
        val subjectCardColors = listOf(gradient1, gradient2, gradient3, gradient4, gradient5)
    }
}
