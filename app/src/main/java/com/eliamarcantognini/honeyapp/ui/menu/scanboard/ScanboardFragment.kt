package com.eliamarcantognini.honeyapp.ui.menu.scanboard

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eliamarcantognini.honeyapp.R
import com.eliamarcantognini.honeyapp.databinding.ScanboardFragmentBinding
import com.eliamarcantognini.honeyapp.databinding.ScannerFragmentBinding

class ScanboardFragment : Fragment() {

    companion object {
        fun newInstance() = ScanboardFragment()
    }

    private lateinit var viewModel: ScanboardViewModel
    private var _binding : ScanboardFragmentBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ScanboardFragmentBinding.inflate(inflater, container, false)
        val root : View = binding.root

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ScanboardViewModel::class.java)
        // TODO: Use the ViewModel
    }

}