package com.example.prepdeck.presentation.session

import com.example.prepdeck.domain.model.Session
import com.example.prepdeck.domain.model.Subject

sealed class SessionEvent {
    data class OnRelatedSubjectChange(val subject: Subject) : SessionEvent()
    data object StartSession : SessionEvent()
    data object CancelSession : SessionEvent()
    data object FinishSession : SessionEvent()
    data class OnDeleteSessionButtonClick(val session: Session) : SessionEvent()
    data object DeleteSession : SessionEvent()
}
