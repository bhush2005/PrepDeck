package com.example.prepdeck.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.prepdeck.domain.model.Session
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {

    @Insert
    suspend fun insertSession(session: Session)

    @Delete
    suspend fun deleteSession(session: Session)

    @Query("SELECT * FROM session")
    fun getALlSessions(): Flow<List<Session>>

    @Query("SELECT * FROM session WHERE sessionSubjectId = :subjectID")
    fun getRecentSessionsForSubject(subjectID: Int): Flow<List<Session>>

    @Query("SELECT SUM(duration) FROM Session")
    fun getTotalSessionsDuration(): Flow<Long>

    @Query("SELECT SUM(duration) FROM Session WHERE sessionSubjectId = :subjectID")
    fun getTotalSessionsDurationBySubjectID(subjectID: Int): Flow<Long>

    @Query("DELETE FROM Session WHERE sessionSubjectId = :subjectID")
    fun deleteSessionsBySubjectID(subjectID: Int)
}