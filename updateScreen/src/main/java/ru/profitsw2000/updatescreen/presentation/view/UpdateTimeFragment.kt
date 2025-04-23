package ru.profitsw2000.updatescreen.presentation.view

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Observer
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.profitsw2000.core.utils.bluetoothbroadcastreceiver.BluetoothStateBroadcastReceiver
import ru.profitsw2000.core.utils.bluetoothbroadcastreceiver.OnBluetoothStateListener
import ru.profitsw2000.data.model.BluetoothConnectionStatus
import ru.profitsw2000.data.model.BluetoothState
import ru.profitsw2000.updatescreen.R
import ru.profitsw2000.updatescreen.databinding.FragmentUpdateTimeBinding
import ru.profitsw2000.updatescreen.presentation.viewmodel.UpdateTimeViewModel

class UpdateTimeFragment : Fragment() {

    private val TAG = "VVV"
    private var _binding: FragmentUpdateTimeBinding? = null
    private val binding get() = _binding!!
    private val updateTimeViewModel: UpdateTimeViewModel by viewModel()
    private var bluetoothIsEnabled = false
    private val requestCodeForEnable = 1
    private val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                switchBluetooth()
            } else {
                showRationaleDialog()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(BluetoothPairedDevicesListFragment.REQUEST_KEY) { _, bundle ->
            val selectedDeviceIndex = bundle.getInt(BluetoothPairedDevicesListFragment.RESULT_EXTRA_KEY)
            if (selectedDeviceIndex != -1) {
                updateTimeViewModel.connectSelectedDevice(selectedDeviceIndex)
            } else {
                updateTimeViewModel.setCurrentState(BluetoothConnectionStatus.Disconnected)
            }
        }
        updateTimeViewModel.initBluetooth(bluetoothPermissionIsGranted())
        lifecycle.addObserver(updateTimeViewModel)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUpdateTimeBinding.bind(inflater.inflate(R.layout.fragment_update_time, container, false))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_update_time, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.connect -> {
                updateTimeViewModel.deviceConnection()
                true
            }
            R.id.bluetooth -> {
                bluetoothOperation()
                true
            }
            else -> true
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)

        if (bluetoothIsEnabled)
            menu.findItem(R.id.bluetooth).setIcon(ru.profitsw2000.core.R.drawable.bt_on)
        else
            menu.findItem(R.id.bluetooth).setIcon(ru.profitsw2000.core.R.drawable.bt_off)

        menu.findItem(R.id.connect).setIcon(getResourceId(updateTimeViewModel.bluetoothConnectionStatus.value))
    }

    private fun observeData() {
        observeDateData()
        observeTimeData()
        observeBluetoothStateData()
        observeBluetoothConnectionStatus()
    }

    private fun observeDateData() {
        val observer = Observer<String> { renderDateData(it) }
        updateTimeViewModel.dateLiveData.observe(viewLifecycleOwner, observer)
    }

    private fun renderDateData(dateString: String) = with(binding) {
        dateTextView.text = dateString
    }

    private fun observeTimeData() {
        val observer = Observer<String> { renderTimeData(it) }
        updateTimeViewModel.timeLiveData.observe(viewLifecycleOwner, observer)
    }

    private fun renderTimeData(timeString: String) = with(binding) {
        timeTextView.text = timeString
    }

    private fun observeBluetoothStateData() {
        val observer = Observer<Boolean> { renderBluetoothStateData(it) }
        updateTimeViewModel.bluetoothIsEnabledData.observe(viewLifecycleOwner, observer)
    }

    private fun renderBluetoothStateData(isEnabled: Boolean) {
        bluetoothIsEnabled = isEnabled
        requireActivity().invalidateOptionsMenu()
    }

    private fun observeBluetoothConnectionStatus() {
        val observer = Observer<BluetoothConnectionStatus> { renderBluetoothConnectionStatusData(it) }
        updateTimeViewModel.bluetoothConnectionStatus.observe(viewLifecycleOwner, observer)
    }

    private fun renderBluetoothConnectionStatusData(bluetoothConnectionStatus: BluetoothConnectionStatus) {
        when(bluetoothConnectionStatus) {
            BluetoothConnectionStatus.Connected -> {
                setButtonState(true)
                binding.progressBar.visibility = View.GONE
            }
            BluetoothConnectionStatus.Connecting -> {
                setButtonState(false)
                binding.progressBar.visibility = View.VISIBLE
            }
            BluetoothConnectionStatus.DeviceSelection -> startBottomSheetDialog()
            BluetoothConnectionStatus.Disconnected -> setButtonState(false)
            BluetoothConnectionStatus.Failed -> {
                setButtonState(false)
                binding.progressBar.visibility = View.GONE
            }
        }
        requireActivity().invalidateOptionsMenu()
    }

    private fun startBottomSheetDialog() {
        val bluetoothPairedDevicesListFragment = BluetoothPairedDevicesListFragment()

        setButtonState(false)
        bluetoothPairedDevicesListFragment.show(parentFragmentManager, "devices list")
    }

    private fun setButtonState(isEnabled: Boolean) = with(binding) {
        updateTimeButton.isEnabled = isEnabled
    }

    private fun getResourceId(bluetoothConnectionStatus: BluetoothConnectionStatus?): Int {
        return when(bluetoothConnectionStatus){
            BluetoothConnectionStatus.Connected -> ru.profitsw2000.core.R.drawable.icon_connected
            BluetoothConnectionStatus.Connecting -> ru.profitsw2000.core.R.drawable.icon_connecting
            BluetoothConnectionStatus.DeviceSelection -> ru.profitsw2000.core.R.drawable.icon_disonnected
            BluetoothConnectionStatus.Disconnected -> ru.profitsw2000.core.R.drawable.icon_disonnected
            BluetoothConnectionStatus.Failed -> ru.profitsw2000.core.R.drawable.icon_failed
            else -> ru.profitsw2000.core.R.drawable.icon_disonnected
        }
    }

    private fun bluetoothOperation() {
        if (VERSION.SDK_INT > VERSION_CODES.R) {
            getBluetoothPermission()
        } else {
            switchBluetooth()
        }
    }

    private fun getBluetoothPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireActivity(),
                android.Manifest.permission.BLUETOOTH_CONNECT
            ) == PackageManager.PERMISSION_GRANTED ->
                switchBluetooth()

            //////////////////////////////////////////////////////////////////

            shouldShowRequestPermissionRationale(android.Manifest.permission.BLUETOOTH_CONNECT) -> showRationaleDialog()

            //////////////////////////////////////////////////////////////////

            else -> requestPermissionLauncher.launch(android.Manifest.permission.BLUETOOTH_CONNECT)
        }
    }

    private fun showRationaleDialog() {
        showSimpleDialog(messageTitle = getString(R.string.bluetooth_permission_rationale_dialog_title),
            messageText = getString(R.string.bluetooth_permission_rationale_dialog_text),
            buttonText = getString(R.string.ok_dialog_button_text))
    }

    private fun showBluetoothEnablingDialog() {
        showSimpleDialog(messageTitle = getString(R.string.bluetooth_on_warning_dialog_title),
            messageText = getString(R.string.bluetooth_on_warning_dialog_text),
            buttonText = getString(R.string.ok_dialog_button_text))
    }

    private fun showSimpleDialog(messageTitle: String,
                                 messageText: String,
                                 buttonText: String) {
        MaterialAlertDialogBuilder(requireActivity())
            .setTitle(messageTitle)
            .setMessage(messageText)
            .setNeutralButton(buttonText) { dialog, _ -> dialog.dismiss()}
            .create()
            .show()
    }


    @SuppressLint("MissingPermission")
    private fun switchBluetooth() {
        if (bluetoothIsEnabled) {
            updateTimeViewModel.disableBluetooth()
        } else {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, requestCodeForEnable)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == requestCodeForEnable) {
            if (resultCode == Activity.RESULT_CANCELED) {
                showBluetoothEnablingDialog()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun bluetoothPermissionIsGranted(): Boolean {
        return if (VERSION.SDK_INT > VERSION_CODES.R) {
            ContextCompat.checkSelfPermission(
                    requireActivity(),
                    android.Manifest.permission.BLUETOOTH_CONNECT
                ) == PackageManager.PERMISSION_GRANTED
        } else true
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = UpdateTimeFragment()
    }
}