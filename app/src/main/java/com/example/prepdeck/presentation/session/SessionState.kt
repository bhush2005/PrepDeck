package com.example.prepdeck.presentation.session

import com.example.prepdeck.domain.model.Session
import com.example.prepdeck.domain.model.Subject

data class SessionState(
    val subjects: List<Subject> = emptyList(),
    val sessions: List<Session> = emptyList(),
    val relatedToSubject: String = "",
    val subjectId: Int = -1,
    val sessionDurationSeconds: Long = 0L,
    val timerState: TimerState = TimerState.IDLE,
    val session: Session? = null
)

enum class TimerState {
    IDLE,
    STARTED,
    STOPPED
}
