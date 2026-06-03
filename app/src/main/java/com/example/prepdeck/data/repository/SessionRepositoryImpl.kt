package com.example.prepdeck.data.repository

import com.example.prepdeck.data.local.SessionDao
import com.example.prepdeck.domain.model.Session
import com.example.prepdeck.domain.repository.SessionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SessionRepositoryImpl @Inject constructor(
    private val sessionDao: SessionDao
): SessionRepository {

    override suspend fun insertSession(session: Session) {
        sessionDao.insertSession(session)
    }

    override suspend fun deleteSession(session: Session) {
        sessionDao.deleteSession(session)
    }

    override fun getALlSessions(): Flow<List<Session>> {
        return sessionDao.getALlSessions()
    }

    override fun getRecentFiveSessions(): Flow<List<Session>> {
        return sessionDao.getALlSessions().map { sessions ->
            sessions.sortedByDescending { it.date }.take(5)
        }
    }

    override fun getRecentTenSessions(): Flow<List<Session>> {
        return sessionDao.getALlSessions().map { sessions ->
            sessions.sortedByDescending { it.date }.take(10)
        }
    }

    override fun getTotalSessionsDuration(): Flow<Long> {
        return sessionDao.getTotalSessionsDuration()
    }

    override fun getTotalSessionsDurationBySubjectID(subjectID: Int): Flow<Long> {
        return sessionDao.getTotalSessionsDurationBySubjectID(subjectID)
    }
}