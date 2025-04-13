package ru.profitsw2000.updatescreen.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.profitsw2000.core.utils.listeners.OnDeviceNameClickListener
import ru.profitsw2000.updatescreen.databinding.FragmentBluetoothPairedDevicesListBinding
import ru.profitsw2000.updatescreen.presentation.view.adapter.BluetoothDevicesListAdapter
import ru.profitsw2000.updatescreen.presentation.viewmodel.UpdateTimeViewModel

class BluetoothPairedDevicesListFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentBluetoothPairedDevicesListBinding? = null
    private val binding get() = _binding!!
    private val updateTimeViewModel: UpdateTimeViewModel by viewModel()
    private val adapter: BluetoothDevicesListAdapter by lazy {
        BluetoothDevicesListAdapter(
            onDeviceNameClickListener = object : OnDeviceNameClickListener{
                override fun onClick(index: Int) {
                    setFragmentResult(REQUEST_KEY,
                        bundleOf(RESULT_EXTRA_KEY to index)
                    )
                    //Toast.makeText(requireActivity(), "Device number $index selected", Toast.LENGTH_SHORT).show()
                    this@BluetoothPairedDevicesListFragment.dismiss()
                }
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentBluetoothPairedDevicesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomSheetBehavior = BottomSheetBehavior.from(view.parent as View)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        val layout: CoordinatorLayout = binding.rootCoordinatorLayout
        layout.minimumHeight = 1500

        initViews()
        observeData()
        updateTimeViewModel.getPairedDevicesStringList()
    }

    private fun initViews() = with(binding) {
        bluetoothDevicesListRecyclerView.adapter = adapter
        bluetoothDevicesListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeData() {
        val observer = Observer<List<String>> { adapter.setData(it) }
        updateTimeViewModel.pairedDevicesStringList.observe(viewLifecycleOwner, observer)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val REQUEST_KEY = "device_key"
        const val RESULT_EXTRA_KEY = "extra_key"

        @JvmStatic
        fun newInstance() = BluetoothPairedDevicesListFragment()
    }
}