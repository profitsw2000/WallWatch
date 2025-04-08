package ru.profitsw2000.data.data

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.IntentFilter
import android.os.Build
import android.os.Build.VERSION
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.profitsw2000.core.utils.bluetoothbroadcastreceiver.BluetoothStateBroadcastReceiver
import ru.profitsw2000.core.utils.bluetoothbroadcastreceiver.OnBluetoothStateListener
import ru.profitsw2000.data.domain.BluetoothRepository
import ru.profitsw2000.data.model.BluetoothState

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

    override fun initBluetooth(permissionIsGranted: Boolean) {
        if (permissionIsGranted) {
            mutableBluetoothEnabledData.value = bluetoothAdapter.isEnabled
        }
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
        if (VERSION.SDK_INT <= Build.VERSION_CODES.S) bluetoothAdapter.disable()
    }

}