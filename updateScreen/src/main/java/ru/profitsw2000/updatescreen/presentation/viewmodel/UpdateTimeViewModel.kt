package ru.profitsw2000.updatescreen.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import ru.profitsw2000.data.domain.DateTimeRepository

class UpdateTimeViewModel(
    private val dateTimeRepository: DateTimeRepository
) : ViewModel() {

    val dateLiveData: LiveData<String> = dateTimeRepository.dateDataString.asLiveData()
    val timeLiveData: LiveData<String> = dateTimeRepository.timeDataString.asLiveData()


}