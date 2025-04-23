package ru.profitsw2000.data.model

sealed class BluetoothState {
    data object BluetoothError : BluetoothState()
    data object BluetoothIsEnabled : BluetoothState()
    data object BluetoothIsDisabled : BluetoothState()
}