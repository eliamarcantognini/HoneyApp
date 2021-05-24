package com.eliamarcantognini.honeyapp.ui.menu.scanner

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eliamarcantognini.honeyapp.R
import com.eliamarcantognini.honeyapp.databinding.HomeFragmentBinding
import com.eliamarcantognini.honeyapp.databinding.ScannerFragmentBinding

class ScannerFragment : Fragment() {

    companion object {
        fun newInstance() = ScannerFragment()
    }

    private lateinit var viewModel: ScannerViewModel
    private var _binding : ScannerFragmentBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ScannerFragmentBinding.inflate(inflater, container, false)
        val root : View = binding.root

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ScannerViewModel::class.java)
        // TODO: Use the ViewModel
    }

}