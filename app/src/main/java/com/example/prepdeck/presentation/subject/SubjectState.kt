package com.example.prepdeck.presentation.subject

import androidx.compose.ui.graphics.Color
import com.example.prepdeck.domain.model.Session
import com.example.prepdeck.domain.model.Subject
import com.example.prepdeck.domain.model.Task

data class SubjectState(
    val currentSubjectId: Int? = null,
    val subjectName: String = "",
    val goalStudyHours: String = "",
    val subjectCardColors: List<Color> = Subject.subjectCardColors.random(),
    val studiedHours: Float = 0f,
    val progress: Float = 0f,
    val upcomingTasks: List<Task> = emptyList(),
    val completedTasks: List<Task> = emptyList(),
    val recentSessions: List<Session> = emptyList(),
    val session: Session? = null
)
