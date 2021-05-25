package com.eliamarcantognini.honeyapp.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.eliamarcantognini.honeyapp.R

class ScanResultFragment : Fragment() {

    companion object {
        fun newInstance() = ScanResultFragment()
    }

    private lateinit var viewModel: ScanResultViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.scan_result_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ScanResultViewModel::class.java)
        // TODO: Use the ViewModel
    }

}