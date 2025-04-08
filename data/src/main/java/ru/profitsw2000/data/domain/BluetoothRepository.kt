package ru.profitsw2000.data.domain

import kotlinx.coroutines.flow.StateFlow
import ru.profitsw2000.core.utils.bluetoothbroadcastreceiver.BluetoothStateBroadcastReceiver
import ru.profitsw2000.data.model.BluetoothState

interface BluetoothRepository {

    val bluetoothIsEnabledData: StateFlow<Boolean>
    val bluetoothStateBroadcastReceiver: BluetoothStateBroadcastReceiver

    fun initBluetooth(permissionIsGranted: Boolean)

    fun registerReceiver()

    fun unregisterReceiver()

    fun disableBluetooth()

}