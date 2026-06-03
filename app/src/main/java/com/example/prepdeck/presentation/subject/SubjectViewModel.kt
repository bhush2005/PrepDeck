package com.example.prepdeck.presentation.subject

import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prepdeck.domain.model.Subject
import com.example.prepdeck.domain.repository.SessionRepository
import com.example.prepdeck.domain.repository.SubjectRepository
import com.example.prepdeck.domain.repository.TaskRepository
import com.example.prepdeck.util.toHours
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubjectViewModel @Inject constructor(
    private val subjectRepository: SubjectRepository,
    private val taskRepository: TaskRepository,
    private val sessionRepository: SessionRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val subjectId: Int = checkNotNull(savedStateHandle["subjectID"])

    private val _state = MutableStateFlow(SubjectState())
    val state = combine(
        _state,
        taskRepository.getUpcomingTasksForSubject(subjectId),
        taskRepository.getCompletedTasksForSubject(subjectId),
        sessionRepository.getRecentTenSessions(),
        sessionRepository.getTotalSessionsDurationBySubjectID(subjectId)
    ) { state, upcomingTasks, completedTasks, recentSessions, totalDuration ->
        state.copy(
            upcomingTasks = upcomingTasks,
            completedTasks = completedTasks,
            recentSessions = recentSessions.filter { it.sessionSubjectId == subjectId },
            studiedHours = totalDuration.toHours(),
            progress = if (state.goalStudyHours.toFloatOrNull() == null || state.goalStudyHours.toFloat() == 0f) 0f
                       else (totalDuration.toHours() / state.goalStudyHours.toFloat()).coerceIn(0f, 1f)
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SubjectState()
    )

    init {
        fetchSubject()
    }

    fun onEvent(event: SubjectEvent) {
        when (event) {
            is SubjectEvent.OnSubjectNameChange -> {
                _state.update { it.copy(subjectName = event.name) }
            }
            is SubjectEvent.OnGoalStudyHoursChange -> {
                _state.update { it.copy(goalStudyHours = event.hours) }
            }
            is SubjectEvent.OnSubjectCardColorChange -> {
                _state.update { it.copy(subjectCardColors = event.colors) }
            }
            SubjectEvent.UpdateSubject -> updateSubject()
            SubjectEvent.DeleteSubject -> deleteSubject()
            is SubjectEvent.OnTaskIsCompletedChange -> {
                viewModelScope.launch {
                    taskRepository.upsertTask(
                        task = event.task.copy(isComplete = !event.task.isComplete)
                    )
                }
            }
            is SubjectEvent.OnDeleteSessionButtonClick -> {
                _state.update { it.copy(session = event.session) }
            }
            SubjectEvent.DeleteSession -> deleteSession()
        }
    }

    private fun fetchSubject() {
        viewModelScope.launch {
            subjectRepository.getSubject(subjectId)?.let { subject ->
                _state.update {
                    it.copy(
                        subjectName = subject.name,
                        goalStudyHours = subject.goalHours.toString(),
                        subjectCardColors = subject.colors.map { color ->
                            androidx.compose.ui.graphics.Color(color)
                        },
                        currentSubjectId = subject.subjectId
                    )
                }
            }
        }
    }

    private fun updateSubject() {
        viewModelScope.launch {
            subjectRepository.upsertSubject(
                subject = Subject(
                    subjectId = subjectId,
                    name = _state.value.subjectName,
                    goalHours = _state.value.goalStudyHours.toFloatOrNull() ?: 1f,
                    colors = _state.value.subjectCardColors.map { it.toArgb() }
                )
            )
        }
    }

    private fun deleteSubject() {
        viewModelScope.launch(Dispatchers.IO) {
            subjectRepository.deleteSubject(subjectId)
        }
    }

    private fun deleteSession() {
        viewModelScope.launch {
            _state.value.session?.let { session ->
                sessionRepository.deleteSession(session)
                _state.update { it.copy(session = null) }
            }
        }
    }
}