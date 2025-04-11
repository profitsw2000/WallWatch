package ru.profitsw2000.updatescreen.presentation.viewmodel

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import ru.profitsw2000.data.domain.BluetoothRepository
import ru.profitsw2000.data.domain.DateTimeRepository
import ru.profitsw2000.data.model.BluetoothState

class UpdateTimeViewModel(
    private val dateTimeRepository: DateTimeRepository,
    private val bluetoothRepository: BluetoothRepository
) : ViewModel(), DefaultLifecycleObserver {

    val dateLiveData: LiveData<String> = dateTimeRepository.dateDataString.asLiveData()
    val timeLiveData: LiveData<String> = dateTimeRepository.timeDataString.asLiveData()
    val bluetoothIsEnabledData: LiveData<Boolean> = bluetoothRepository.bluetoothIsEnabledData.asLiveData()
    val pairedDevicesList: LiveData<List<String>> = bluetoothRepository.bluetoothPairedDevicesStringList.asLiveData()

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        bluetoothRepository.registerReceiver()
    }

    fun initBluetooth(permissionIsGranted: Boolean) = bluetoothRepository.initBluetooth(permissionIsGranted)

    fun disableBluetooth() {
        bluetoothRepository.disableBluetooth()
    }

    fun getPairedDevicesStringList() {
        bluetoothRepository.getPairedDevicesStringList()
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        bluetoothRepository.unregisterReceiver()
    }

}