package com.example.prepdeck.presentation.task

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prepdeck.domain.model.Task
import com.example.prepdeck.domain.repository.SubjectRepository
import com.example.prepdeck.domain.repository.TaskRepository
import com.example.prepdeck.util.Priority
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val subjectRepository: SubjectRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val taskId: Int? = savedStateHandle["taskID"]
    private val subjectId: Int? = savedStateHandle["subjectID"]

    private val _state = MutableStateFlow(TaskState())
    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TaskState()
    )

    init {
        fetchSubjects()
        fetchTask()
    }

    fun onEvent(event: TaskEvent) {
        when (event) {
            is TaskEvent.OnTitleChange -> _state.update { it.copy(title = event.title) }
            is TaskEvent.OnDescriptionChange -> _state.update { it.copy(description = event.description) }
            is TaskEvent.OnDateChange -> _state.update { it.copy(dueDate = event.millis) }
            is TaskEvent.OnPriorityChange -> _state.update { it.copy(priority = event.priority) }
            is TaskEvent.OnRelatedSubjectSelect -> {
                _state.update {
                    it.copy(
                        relatedToSubject = event.subject.name,
                        currentTaskSubjectId = event.subject.subjectId ?: -1
                    )
                }
            }
            TaskEvent.OnIsCompleteStatusChange -> {
                _state.update { it.copy(isTaskComplete = !it.isTaskComplete) }
            }
            TaskEvent.SaveTask -> saveTask()
            TaskEvent.DeleteTask -> deleteTask()
        }
    }

    private fun fetchSubjects() {
        viewModelScope.launch {
            subjectRepository.getAllSubjects().collect { subjects ->
                _state.update { it.copy(subjectList = subjects) }
            }
        }
    }

    private fun fetchTask() {
        viewModelScope.launch {
            // If editing an existing task, load its data
            taskId?.let { id ->
                taskRepository.getTaskById(id)?.let { task ->
                    _state.update {
                        it.copy(
                            title = task.title,
                            description = task.description,
                            dueDate = task.dueDate,
                            priority = Priority.fromInt(task.priority),
                            relatedToSubject = task.relatedToSubject,
                            currentTaskSubjectId = task.taskSubjectId,
                            isTaskComplete = task.isComplete,
                            currentTask = task
                        )
                    }
                }
            }
            // If coming from a subject screen, pre-fill subjectId
            subjectId?.let { id ->
                if (id != -1 && _state.value.currentTask == null) {
                    subjectRepository.getSubject(id)?.let { subject ->
                        _state.update {
                            it.copy(
                                relatedToSubject = subject.name,
                                currentTaskSubjectId = id
                            )
                        }
                    }
                }
            }
        }
    }

    private fun saveTask() {
        viewModelScope.launch {
            val state = _state.value
            taskRepository.upsertTask(
                task = Task(
                    taskId = state.currentTask?.taskId,
                    title = state.title,
                    description = state.description,
                    dueDate = state.dueDate ?: Instant.now().toEpochMilli(),
                    priority = state.priority.value,
                    relatedToSubject = state.relatedToSubject,
                    isComplete = state.isTaskComplete,
                    taskSubjectId = state.currentTaskSubjectId
                )
            )
        }
    }

    private fun deleteTask() {
        viewModelScope.launch {
            _state.value.currentTask?.taskId?.let { id ->
                taskRepository.deleteTask(id)
            }
        }
    }
}