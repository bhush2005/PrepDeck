package com.example.prepdeck.presentation.session

import androidx.lifecycle.ViewModel
import com.example.prepdeck.domain.repository.SubjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val subjectRepository: SubjectRepository,
): ViewModel() {

}