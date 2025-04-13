package ru.profitsw2000.data.data

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.IntentFilter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.profitsw2000.core.utils.bluetoothbroadcastreceiver.BluetoothStateBroadcastReceiver
import ru.profitsw2000.core.utils.bluetoothbroadcastreceiver.OnBluetoothStateListener
import ru.profitsw2000.data.domain.BluetoothRepository

class BluetoothRepositoryImpl(
    private val context: Context
) : BluetoothRepository, OnBluetoothStateListener {

    private val bluetoothManager: BluetoothManager by lazy {
        context.getSystemService(BluetoothManager::class.java)
    }
    private val bluetoothAdapter: BluetoothAdapter by lazy {
        bluetoothManager.adapter
    }
    private val mutableBluetoothEnabledData = MutableStateFlow(false)
    override val bluetoothIsEnabledData: StateFlow<Boolean>
        get() = mutableBluetoothEnabledData
    override val bluetoothStateBroadcastReceiver = BluetoothStateBroadcastReceiver(this)
    private val bluetoothPairedDevicesMutableStringList = MutableStateFlow<List<String>>(listOf())
    override val bluetoothPairedDevicesStringList: StateFlow<List<String>>
        get() = bluetoothPairedDevicesMutableStringList

    override fun initBluetooth() {
            mutableBluetoothEnabledData.value = bluetoothAdapter.isEnabled
    }

    @SuppressLint("MissingPermission")
    override fun getPairedDevicesStringList() {
        val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter.bondedDevices
        val pairedDevicesNameList = arrayListOf<String>()
        pairedDevices?.forEach { device ->
            pairedDevicesNameList.add(device.name)
        }
        bluetoothPairedDevicesMutableStringList.value = pairedDevicesNameList
    }

    override fun onBluetoothStateChanged(bluetoothIsEnabled: Boolean) {
        mutableBluetoothEnabledData.value = bluetoothIsEnabled
    }

    override fun registerReceiver() {
        context.registerReceiver(bluetoothStateBroadcastReceiver, IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED))
    }

    override fun unregisterReceiver() {
        context.unregisterReceiver(bluetoothStateBroadcastReceiver)
    }

    @SuppressLint("MissingPermission")
    override fun disableBluetooth() {
        bluetoothAdapter.disable()
        bluetoothPairedDevicesMutableStringList.value = listOf()
    }

}