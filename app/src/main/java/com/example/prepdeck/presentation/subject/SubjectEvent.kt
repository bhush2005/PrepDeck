package com.example.prepdeck.presentation.subject

import androidx.compose.ui.graphics.Color
import com.example.prepdeck.domain.model.Session
import com.example.prepdeck.domain.model.Task

sealed class SubjectEvent {
    data object UpdateSubject : SubjectEvent()
    data object DeleteSubject : SubjectEvent()
    data class OnSubjectNameChange(val name: String) : SubjectEvent()
    data class OnGoalStudyHoursChange(val hours: String) : SubjectEvent()
    data class OnSubjectCardColorChange(val colors: List<Color>) : SubjectEvent()
    data class OnTaskIsCompletedChange(val task: Task) : SubjectEvent()
    data class OnDeleteSessionButtonClick(val session: Session) : SubjectEvent()
    data object DeleteSession : SubjectEvent()
}
