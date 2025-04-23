package ru.profitsw2000.data.domain

import android.bluetooth.BluetoothDevice
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import ru.profitsw2000.core.utils.bluetoothbroadcastreceiver.BluetoothStateBroadcastReceiver
import ru.profitsw2000.data.model.BluetoothConnectionStatus
import ru.profitsw2000.data.model.BluetoothState

interface BluetoothRepository {

    val bluetoothIsEnabledData: StateFlow<Boolean>
    val bluetoothStateBroadcastReceiver: BluetoothStateBroadcastReceiver
    val bluetoothPairedDevicesStringList: StateFlow<List<String>>

    fun initBluetooth()

    fun getPairedDevicesStringList(): List<BluetoothDevice>

    suspend fun connectDevice(device: BluetoothDevice): BluetoothConnectionStatus

    suspend fun disconnectDevice(): BluetoothConnectionStatus

    fun registerReceiver()

    fun unregisterReceiver()

    fun disableBluetooth()

}