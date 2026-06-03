package com.example.prepdeck.presentation.task

import com.example.prepdeck.domain.model.Subject
import com.example.prepdeck.util.Priority

sealed class TaskEvent {
    data class OnTitleChange(val title: String) : TaskEvent()
    data class OnDescriptionChange(val description: String) : TaskEvent()
    data class OnDateChange(val millis: Long?) : TaskEvent()
    data class OnPriorityChange(val priority: Priority) : TaskEvent()
    data class OnRelatedSubjectSelect(val subject: Subject) : TaskEvent()
    data object OnIsCompleteStatusChange : TaskEvent()
    data object SaveTask : TaskEvent()
    data object DeleteTask : TaskEvent()
}
