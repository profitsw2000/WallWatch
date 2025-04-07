package ru.profitsw2000.data.domain

import kotlinx.coroutines.flow.StateFlow
import ru.profitsw2000.core.utils.bluetoothbroadcastreceiver.BluetoothStateBroadcastReceiver
import ru.profitsw2000.data.model.BluetoothState

interface BluetoothRepository {

    val bluetoothIsEnabledData: StateFlow<BluetoothState>
    val bluetoothStateBroadcastReceiver: BluetoothStateBroadcastReceiver

    fun unregisterReceiver()

}