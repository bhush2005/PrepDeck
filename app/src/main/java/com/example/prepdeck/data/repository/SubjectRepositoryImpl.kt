package com.example.prepdeck.data.repository

import com.example.prepdeck.data.local.SubjectDao
import com.example.prepdeck.domain.model.Subject
import com.example.prepdeck.domain.repository.SubjectRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SubjectRepositoryImpl @Inject constructor(
    private val subjectDao: SubjectDao
): SubjectRepository {

    override suspend fun upsertSubject(subject: Subject) {
        subjectDao.upsertSubject(subject)
    }

    override fun getTotalSubjectCount(): Flow<Int> {
        return subjectDao.getTotalSubjectCount()
    }

    override fun getTotalGoalHours(): Flow<Float> {
        return subjectDao.getTotalGoalHours()
    }

    override suspend fun deleteSubject(subjectInt: Int) {
        subjectDao.deleteSubject(subjectInt)
    }

    override suspend fun getSubject(subjectInt: Int): Subject? {
        return subjectDao.getSubjectById(subjectInt)
    }

    override fun getAllSubjects(): Flow<List<Subject>> {
        return subjectDao.getAllSubjects()
    }
}