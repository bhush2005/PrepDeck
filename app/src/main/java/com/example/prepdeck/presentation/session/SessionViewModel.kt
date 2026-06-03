package com.example.prepdeck.presentation.session

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prepdeck.domain.model.Session
import com.example.prepdeck.domain.repository.SessionRepository
import com.example.prepdeck.domain.repository.SubjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val subjectRepository: SubjectRepository,
    private val sessionRepository: SessionRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(SessionState())
    val state = combine(
        _state,
        subjectRepository.getAllSubjects(),
        sessionRepository.getRecentTenSessions()
    ) { state, subjects, sessions ->
        state.copy(
            subjects = subjects,
            sessions = sessions
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SessionState()
    )

    // Timer coroutine job
    private var timerJob: kotlinx.coroutines.Job? = null

    fun onEvent(event: SessionEvent) {
        when (event) {
            is SessionEvent.OnRelatedSubjectChange -> {
                _state.update {
                    it.copy(
                        relatedToSubject = event.subject.name,
                        subjectId = event.subject.subjectId ?: -1
                    )
                }
            }
            SessionEvent.StartSession -> startTimer()
            SessionEvent.CancelSession -> cancelTimer()
            SessionEvent.FinishSession -> finishSession()
            is SessionEvent.OnDeleteSessionButtonClick -> {
                _state.update { it.copy(session = event.session) }
            }
            SessionEvent.DeleteSession -> deleteSession()
        }
    }

    private fun startTimer() {
        _state.update { it.copy(timerState = TimerState.STARTED) }
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                kotlinx.coroutines.delay(1000L)
                _state.update { it.copy(sessionDurationSeconds = it.sessionDurationSeconds + 1L) }
            }
        }
    }

    private fun cancelTimer() {
        timerJob?.cancel()
        timerJob = null
        _state.update {
            it.copy(
                timerState = TimerState.IDLE,
                sessionDurationSeconds = 0L
            )
        }
    }

    private fun finishSession() {
        val duration = _state.value.sessionDurationSeconds
        if (duration < 36) {
            // Optionally ignore sessions shorter than 36 seconds
            cancelTimer()
            return
        }
        viewModelScope.launch {
            sessionRepository.insertSession(
                Session(
                    sessionSubjectId = _state.value.subjectId,
                    relatedToSubject = _state.value.relatedToSubject,
                    date = Instant.now().toEpochMilli(),
                    duration = duration
                )
            )
            cancelTimer()
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

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}