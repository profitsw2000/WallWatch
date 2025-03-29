package ru.profitsw2000.updatescreen.presentation.view

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import ru.profitsw2000.updatescreen.R
import ru.profitsw2000.updatescreen.databinding.FragmentUpdateTimeBinding

class UpdateTimeFragment : Fragment() {

    private var _binding: FragmentUpdateTimeBinding? = null
    private val binding get() = _binding!!
    private var bluetoothIsOn = false
    private var isConnected = false
    private val bluetoothManager: BluetoothManager by lazy {
        requireActivity().getSystemService(BluetoothManager::class.java)
    }
    private val bluetoothAdapter: BluetoothAdapter by lazy {
        bluetoothManager.adapter
    }
    private val requestCodeForEnable = 1
    val requestPermissionLauncher = registerForActivityResult(
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
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_update_time, menu)
        menu.findItem(R.id.bluetooth).setIcon(ru.profitsw2000.core.R.drawable.bt_on)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //requireActivity().invalidateOptionsMenu()
        return when (item.itemId) {
            R.id.connect -> {
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

        if (bluetoothPermissionIsGranted())
            if (bluetoothAdapter.isEnabled) menu.findItem(R.id.bluetooth).setIcon(ru.profitsw2000.core.R.drawable.bt_on)
            else menu.findItem(R.id.bluetooth).setIcon(ru.profitsw2000.core.R.drawable.bt_off)
        else
            menu.findItem(R.id.bluetooth).setIcon(ru.profitsw2000.core.R.drawable.bt_off)

        if (isConnected)
            menu.findItem(R.id.connect).setIcon(ru.profitsw2000.core.R.drawable.connected)
        else
            menu.findItem(R.id.connect).setIcon(ru.profitsw2000.core.R.drawable.unconnected)
    }

    private fun bluetoothOperation() {
        if (bluetoothAdapter == null) {
            MaterialAlertDialogBuilder(requireActivity())
                .setTitle("Ошибка включения Bluetooth")
                .setMessage("Bluetooth на данном устройстве отсутствует")
                .setPositiveButton("OK") { dialog, _ -> dialog.dismiss()}
                .create()
                .show()
        } else {
            if (VERSION.SDK_INT > VERSION_CODES.R) {
                getBluetoothPermission()
            } else {
                switchBluetooth()
            }
        }
    }

    private fun getBluetoothPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireActivity(),
                android.Manifest.permission.BLUETOOTH_CONNECT
            ) == PackageManager.PERMISSION_GRANTED -> switchBluetoothOn()

            //////////////////////////////////////////////////////////////////

            shouldShowRequestPermissionRationale(android.Manifest.permission.BLUETOOTH_CONNECT) -> showRationaleDialog()

            //////////////////////////////////////////////////////////////////

            else -> requestPermissionLauncher.launch(android.Manifest.permission.BLUETOOTH_CONNECT)
        }
    }

    private fun showRationaleDialog() {
        MaterialAlertDialogBuilder(requireActivity())
            .setTitle("Разрешение Bluetooth")
            .setMessage("При подключении к настенным часам используется Bluetooth. Для синхронизации отображаемого на настенных часах времени " +
                    "необходимо предоставить приложению разрешение на использование Bluetooth.")
            .setNegativeButton("OK") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    private fun showBluetoothEnablingDialog() {
        MaterialAlertDialogBuilder(requireActivity())
            .setTitle("Включение Bluetooth")
            .setMessage("Для синхронизации отображаемого на настенных часах времени необходимо включить Bluetooth.")
            .setNegativeButton("OK") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }


    @SuppressLint("MissingPermission")
    private fun switchBluetooth() {
        if (bluetoothAdapter.isEnabled && VERSION.SDK_INT <= VERSION_CODES.R) {
            bluetoothAdapter.disable()
        } else {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, requestCodeForEnable)
        }
    }


    @SuppressLint("MissingPermission")
    private fun switchBluetoothOn() {
        if (!bluetoothAdapter.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, requestCodeForEnable)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == requestCodeForEnable) {
            if (resultCode == Activity.RESULT_OK) {
                requireActivity().invalidateOptionsMenu()
            } else if (resultCode == Activity.RESULT_CANCELED) {
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

    companion object {
        @JvmStatic
        fun newInstance() = UpdateTimeFragment()
    }
}