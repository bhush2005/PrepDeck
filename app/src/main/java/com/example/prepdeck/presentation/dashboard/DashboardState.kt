package com.example.prepdeck.presentation.dashboard

import androidx.compose.ui.graphics.Color
import com.example.prepdeck.domain.model.Session
import com.example.prepdeck.domain.model.Subject
import com.example.prepdeck.domain.model.Task

data class DashboardState(
    val totalSubjectCount: Int = 0,
    val totalStudiedHours: Float = 0f,
    val totalGoalHours: Float = 0f,
    val subjects: List<Subject> = emptyList(),
    val upcomingTasks: List<Task> = emptyList(),
    val recentSessions: List<Session> = emptyList(),
    val subjectName: String = "",
    val goalStudyHours: String = "",
    val SubjectCardColors: List<Color> = Subject.subjectCardColors.random(),
    val session: Session? = null
)
