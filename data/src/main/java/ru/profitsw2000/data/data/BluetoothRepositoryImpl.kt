package ru.profitsw2000.data.data

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.IntentFilter
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
    private val mutableBluetoothEnabledData = MutableStateFlow<BluetoothState>(BluetoothState.BluetoothIsDisabled)
    override val bluetoothIsEnabledData: StateFlow<BluetoothState>
        get() = mutableBluetoothEnabledData
    override val bluetoothStateBroadcastReceiver = BluetoothStateBroadcastReceiver(this)

    init {
        context.registerReceiver(bluetoothStateBroadcastReceiver, IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED))
    }

    override fun onBluetoothStateChanged(bluetoothIsEnabled: Boolean) {
        mutableBluetoothEnabledData.value = if (bluetoothIsEnabled) BluetoothState.BluetoothIsEnabled
        else BluetoothState.BluetoothIsDisabled
    }

    override fun unregisterReceiver() {
        context.unregisterReceiver(bluetoothStateBroadcastReceiver)
    }

    fun switchBluetooth() {
        if (bluetoothAdapter == null) mutableBluetoothEnabledData.value = BluetoothState.BluetoothError
        else
    }

}