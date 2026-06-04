package com.example.prepdeck.presentation.session

import com.example.prepdeck.domain.model.Session
import com.example.prepdeck.domain.model.Subject

data class SessionState(
    val subjects: List<Subject> = emptyList(),
    val sessions: List<Session> = emptyList(),
    val relatedToSubject: String? = null,
    val subjectId: Int? = null,
    val session: Session? = null
)
