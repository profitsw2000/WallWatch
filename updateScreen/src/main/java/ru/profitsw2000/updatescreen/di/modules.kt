package ru.profitsw2000.updatescreen.di

import android.bluetooth.BluetoothManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val updateTimeModule = module {

    single {
        androidContext().getSystemService(BluetoothManager::class.java).adapter
    }



}