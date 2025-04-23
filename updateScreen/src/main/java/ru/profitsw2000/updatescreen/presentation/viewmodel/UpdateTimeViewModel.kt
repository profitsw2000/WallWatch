package ru.profitsw2000.updatescreen.presentation.viewmodel

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.os.Build
import android.os.Build.VERSION
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.profitsw2000.data.domain.BluetoothRepository
import ru.profitsw2000.data.domain.DateTimeRepository
import ru.profitsw2000.data.model.BluetoothConnectionStatus

class UpdateTimeViewModel(
    private val dateTimeRepository: DateTimeRepository,
    private val bluetoothRepository: BluetoothRepository
) : ViewModel(), DefaultLifecycleObserver {

    val dateLiveData: LiveData<String> = dateTimeRepository.dateDataString.asLiveData()
    val timeLiveData: LiveData<String> = dateTimeRepository.timeDataString.asLiveData()
    val bluetoothIsEnabledData: LiveData<Boolean> = bluetoothRepository.bluetoothIsEnabledData.asLiveData()
    val pairedDevicesStringList: LiveData<List<String>> = bluetoothRepository.bluetoothPairedDevicesStringList.asLiveData()
    private lateinit var pairedDevicesList: List<BluetoothDevice>
    private var _bluetoothConnectionStatus: MutableLiveData<BluetoothConnectionStatus> = MutableLiveData(BluetoothConnectionStatus.Disconnected)
    val bluetoothConnectionStatus by this::_bluetoothConnectionStatus
    private val scope = CoroutineScope(Dispatchers.Main)


    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        bluetoothRepository.registerReceiver()
    }

    fun initBluetooth(permissionIsGranted: Boolean) {
        if (permissionIsGranted) bluetoothRepository.initBluetooth()
    }

    @SuppressLint("SuspiciousIndentation")
    fun disableBluetooth() {
        if (VERSION.SDK_INT <= Build.VERSION_CODES.S){
            if (bluetoothConnectionStatus.value == BluetoothConnectionStatus.Disconnected ||
                bluetoothConnectionStatus.value == BluetoothConnectionStatus.Failed)
            bluetoothRepository.disableBluetooth()
        }
    }

    fun deviceConnection() {
        if (bluetoothIsEnabledData.value == true) {
            when(bluetoothConnectionStatus.value) {
                BluetoothConnectionStatus.Disconnected -> bluetoothConnectionStatus.value = BluetoothConnectionStatus.DeviceSelection
                BluetoothConnectionStatus.DeviceSelection -> bluetoothConnectionStatus.value = BluetoothConnectionStatus.DeviceSelection
                BluetoothConnectionStatus.Connected -> disconnectDevice()
                BluetoothConnectionStatus.Failed -> bluetoothConnectionStatus.value = BluetoothConnectionStatus.DeviceSelection
                else -> {}
            }
        }
    }

    fun setCurrentState(bluetoothConnectionStatus: BluetoothConnectionStatus) {
        _bluetoothConnectionStatus.value = bluetoothConnectionStatus
    }

    fun connectSelectedDevice(index: Int) {
        _bluetoothConnectionStatus.value = BluetoothConnectionStatus.Connecting
        val device = pairedDevicesList[index]
        scope.launch {
            _bluetoothConnectionStatus.value = bluetoothRepository.connectDevice(device)
        }
    }

    private fun disconnectDevice() {
        scope.launch {
            _bluetoothConnectionStatus.value = bluetoothRepository.disconnectDevice()
        }
    }

    fun getPairedDevicesStringList() {
        if (bluetoothIsEnabledData.value == true) {
            pairedDevicesList = bluetoothRepository.getPairedDevicesStringList()
        }
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        bluetoothRepository.unregisterReceiver()
    }
}