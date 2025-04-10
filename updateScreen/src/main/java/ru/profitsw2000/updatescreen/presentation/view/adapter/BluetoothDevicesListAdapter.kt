package ru.profitsw2000.updatescreen.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.profitsw2000.updatescreen.databinding.BluetoothDeviceItemViewBinding

class BluetoothDevicesListAdapter() : RecyclerView.Adapter<BluetoothDevicesListAdapter.ViewHolder> () {

    private var data: List<String> = arrayListOf()

    fun setData(data: List<String>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = BluetoothDeviceItemViewBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false)
        val bluetoothDeviceViewHolder = ViewHolder(binding)

        return bluetoothDeviceViewHolder
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bluetoothDeviceName = data[position]

        holder.deviceName.text = bluetoothDeviceName
    }

    inner class ViewHolder(binding: BluetoothDeviceItemViewBinding) : RecyclerView.ViewHolder(binding.root) {
        val deviceName = binding.bluetoothDeviceNameTextView
    }
}