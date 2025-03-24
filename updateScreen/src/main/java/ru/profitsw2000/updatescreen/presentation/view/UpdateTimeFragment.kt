package ru.profitsw2000.updatescreen.presentation.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import ru.profitsw2000.updatescreen.R
import ru.profitsw2000.updatescreen.databinding.FragmentUpdateTimeBinding

class UpdateTimeFragment : Fragment() {

    private var _binding: FragmentUpdateTimeBinding? = null
    private val binding get() = _binding!!
    private var bluetoothIsOn = false
    private var isConnected = false

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_update_time, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        requireActivity().invalidateOptionsMenu()
        return when (item.itemId) {
            R.id.connect -> {
                //Toast.makeText(requireActivity(), "Connect to device", Toast.LENGTH_SHORT).show()
                isConnected = !isConnected
                true
            }
            R.id.bluetooth -> {
                //Toast.makeText(requireActivity(), "Switch bluetooth on/off", Toast.LENGTH_SHORT).show()
                bluetoothIsOn = !bluetoothIsOn
                true
            }
            else -> true
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)

        if (bluetoothIsOn)
            menu.findItem(R.id.bluetooth).setIcon(ru.profitsw2000.core.R.drawable.bt_on)
        else
            menu.findItem(R.id.bluetooth).setIcon(ru.profitsw2000.core.R.drawable.bt_off)

        if (isConnected)
            menu.findItem(R.id.connect).setIcon(ru.profitsw2000.core.R.drawable.connected)
        else
            menu.findItem(R.id.connect).setIcon(ru.profitsw2000.core.R.drawable.unconnected)
    }

    companion object {
        @JvmStatic
        fun newInstance() = UpdateTimeFragment()
    }
}