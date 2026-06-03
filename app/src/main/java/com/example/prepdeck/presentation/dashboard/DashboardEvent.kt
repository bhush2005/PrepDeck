package com.example.prepdeck.presentation.dashboard

import androidx.compose.ui.graphics.Color
import com.example.prepdeck.domain.model.Session
import com.example.prepdeck.domain.model.Task

sealed class DashboardEvent {
    data object SaveSubject : DashboardEvent()

    data object DeleteSubject : DashboardEvent()

    data class onDeleteSessionButtonClick(val session: Session) : DashboardEvent()

    data class OnTaskIsCompletedChange(val task: Task) : DashboardEvent()

    data class OnSubjectCardColorChange(val color: List<Color>) : DashboardEvent()

    data class OnSubjectNameChange(val name: String) : DashboardEvent()

    data class OnGoalStudyHoursChange(val hours: String) : DashboardEvent()

}