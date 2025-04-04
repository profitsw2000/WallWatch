package ru.profitsw2000.data.data

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.profitsw2000.data.domain.DateTimeRepository

class DateTimeRepositoryImpl : DateTimeRepository {

    private val mutableDateDataString: MutableStateFlow<String> = MutableStateFlow("05.04 Пт")
    override val dateDataString: StateFlow<String>
        get() = mutableDateDataString
    private val mutableTimeDataString: MutableStateFlow<String> = MutableStateFlow("12:34:56")
    override val timeDataString: StateFlow<String>
        get() = mutableTimeDataString

}