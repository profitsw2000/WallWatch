package ru.profitsw2000.updatescreen.di

import android.bluetooth.BluetoothManager
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.profitsw2000.data.data.DateTimeRepositoryImpl
import ru.profitsw2000.data.domain.DateTimeRepository
import ru.profitsw2000.updatescreen.presentation.viewmodel.UpdateTimeViewModel

val updateTimeModule = module {

/*    single {
        androidContext().getSystemService(BluetoothManager::class.java).adapter
    }*/

    single<DateTimeRepository> { DateTimeRepositoryImpl() }
    single { UpdateTimeViewModel(get()) }

}