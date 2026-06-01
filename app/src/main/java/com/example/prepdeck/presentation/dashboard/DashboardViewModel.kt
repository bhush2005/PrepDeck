package com.example.prepdeck.presentation.dashboard

import androidx.lifecycle.ViewModel
import com.example.prepdeck.domain.repository.SubjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val subjectRepository: SubjectRepository,
): ViewModel() {

    val subjectCount = subjectRepository.getTotalSubjectCount()
    val studiedHours = subjectRepository.getTotalGoalHours()
}