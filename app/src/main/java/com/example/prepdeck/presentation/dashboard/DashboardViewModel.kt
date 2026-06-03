package com.example.prepdeck.presentation.dashboard

import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prepdeck.domain.model.Subject
import com.example.prepdeck.domain.repository.SessionRepository
import com.example.prepdeck.domain.repository.SubjectRepository
import com.example.prepdeck.domain.repository.TaskRepository
import com.example.prepdeck.util.toHours
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val subjectRepository: SubjectRepository,
    private val sessionRepository: SessionRepository,
    private val taskRepository: TaskRepository
): ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state = combine(
        _state,
        subjectRepository.getTotalSubjectCount(),
        subjectRepository.getTotalGoalHours(),
        subjectRepository.getAllSubjects(),
        sessionRepository.getTotalSessionsDuration()
    ){
        state, subjectCount, goalHours, subjects, totalSessionsDuration ->
        state.copy(
            totalSubjectCount = subjectCount,
            totalGoalHours = goalHours,
            subjects = subjects,
            totalStudiedHours = totalSessionsDuration.toHours()
        )
    }.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DashboardState()
    )

    // Separate state flows for tasks and recent sessions
    val upcomingTasks = taskRepository.getAllUpcomingTasks()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val recentSessions = sessionRepository.getRecentTenSessions()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun onEvent(event: DashboardEvent) {
        when (event) {
            is DashboardEvent.onDeleteSessionButtonClick -> {
                _state.update {
                    it.copy(
                        session = event.session
                    )
                }
            }

            is DashboardEvent.OnGoalStudyHoursChange -> {
                _state.update {
                    it.copy(
                        goalStudyHours = event.hours
                    )
                }
            }

            is DashboardEvent.OnSubjectCardColorChange -> {

                _state.update {
                    it.copy(
                        SubjectCardColors = event.color
                    )
                }
            }

            DashboardEvent.SaveSubject -> saveSubject()

            DashboardEvent.DeleteSubject -> deleteSession()
            is DashboardEvent.OnSubjectNameChange -> {
                _state.update {
                    it.copy(
                        subjectName = event.name
                    )
                }
            }
            is DashboardEvent.OnTaskIsCompletedChange -> {
                viewModelScope.launch {
                    taskRepository.upsertTask(
                        task = event.task.copy(
                            isComplete = !event.task.isComplete
                        )
                    )
                }
            }
        }
    }

    private fun saveSubject() {
        viewModelScope.launch {
            subjectRepository.upsertSubject(
                subject = Subject(
                    name = state.value.subjectName,
                    goalHours = state.value.goalStudyHours.toFloatOrNull()?: 1f,
                    colors = state.value.SubjectCardColors.map{it.toArgb()}
                )
            )
            _state.update {
                it.copy(
                    subjectName = "",
                    goalStudyHours = ""
                )
            }
        }
    }

    private fun deleteSession() {
        viewModelScope.launch {
            state.value.session?.let { session ->
                sessionRepository.deleteSession(session)
            }
        }
    }
}