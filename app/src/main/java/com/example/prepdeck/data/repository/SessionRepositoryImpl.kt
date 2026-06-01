package com.example.prepdeck.data.repository

import com.example.prepdeck.data.local.SessionDao
import com.example.prepdeck.domain.model.Session
import com.example.prepdeck.domain.repository.SessionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SessionRepositoryImpl @Inject constructor(
    private val sessionDao: SessionDao

): SessionRepository {
    override suspend fun insertSession(session: Session) {
        sessionDao.insertSession(session)
    }

    override suspend fun deleteSession(session: Session) {
        TODO("Not yet implemented")
    }

    override fun getALlSessions(): Flow<List<Session>> {
        TODO("Not yet implemented")
    }

    override fun getRecentFiveSessions(): Flow<List<Session>> {
        TODO("Not yet implemented")
    }

    override fun getRecentTenSessions(): Flow<List<Session>> {
        TODO("Not yet implemented")
    }

    override fun getTotalSessionsDuration(): Flow<Long> {
        TODO("Not yet implemented")
    }

    override fun getTotalSessionsDurationBySubjectID(subjectID: Int): Flow<Long> {
        TODO("Not yet implemented")
    }
}