package com.example.prepdeck.di

import android.app.Application
import androidx.room.Room
import com.example.prepdeck.data.local.AppDataBase
import com.example.prepdeck.data.local.SessionDao
import com.example.prepdeck.data.local.SubjectDao
import com.example.prepdeck.data.local.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {


    @Provides
    @Singleton
    fun provideDatabase(
        application: Application
    ): AppDataBase {
        return Room
            .databaseBuilder(
                application,
                AppDataBase::class.java,
                "prepdeck.db"
            )
            .build()
    }

    @Provides
    @Singleton
    fun provideSubjectDao(database: AppDataBase): SubjectDao {
        return database.subjectDao()

    }

    @Provides
    @Singleton
    fun provideTaskDao(database: AppDataBase): TaskDao {
        return database.taskDao()

    }

    @Provides
    @Singleton
    fun provideSessionDao(database: AppDataBase): SessionDao {
        return database.sessionDao()

    }
}