package com.example.prepdeck.data.repository

import com.example.prepdeck.data.local.TaskDao
import com.example.prepdeck.domain.model.Task
import com.example.prepdeck.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao
): TaskRepository {

    override suspend fun upsertTask(task: Task) {
        taskDao.upsertTask(task)
    }

    override suspend fun deleteTask(taskId: Int) {
        taskDao.deleteTask(taskId)
    }

    override suspend fun getTaskById(taskId: Int): Task? {
        return taskDao.getTaskById(taskId)
    }

    override fun getUpcomingTasksForSubject(subjectInt: Int): Flow<List<Task>> {
        return taskDao.getTasksForSubject(subjectInt).map { tasks ->
            tasks.filter { !it.isComplete }
        }
    }

    override fun getCompletedTasksForSubject(subjectInt: Int): Flow<List<Task>> {
        return taskDao.getTasksForSubject(subjectInt).map { tasks ->
            tasks.filter { it.isComplete }
        }
    }

    override fun getAllUpcomingTasks(): Flow<List<Task>> {
        return taskDao.getAllTasks().map { tasks ->
            tasks.filter { !it.isComplete }
        }
    }
}
