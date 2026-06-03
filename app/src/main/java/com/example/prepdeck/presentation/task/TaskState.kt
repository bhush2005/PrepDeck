package com.example.prepdeck.presentation.task

import com.example.prepdeck.domain.model.Subject
import com.example.prepdeck.domain.model.Task
import com.example.prepdeck.util.Priority

data class TaskState(
    val title: String = "",
    val description: String = "",
    val dueDate: Long? = null,
    val priority: Priority = Priority.LOW,
    val relatedToSubject: String = "",
    val currentTaskSubjectId: Int = -1,
    val currentTask: Task? = null,
    val isTaskComplete: Boolean = false,
    val subjectList: List<Subject> = emptyList()
)
