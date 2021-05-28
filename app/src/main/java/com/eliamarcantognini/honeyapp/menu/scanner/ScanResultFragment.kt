package com.eliamarcantognini.honeyapp.menu.scanner

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.eliamarcantognini.honeyapp.databinding.ScanResultFragmentBinding

class ScanResultFragment : Fragment() {

    private lateinit var viewModel: ScannerViewModel
    private var _binding: ScanResultFragmentBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // This callback will only be called when MyFragment is at least Started.
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            parentFragment?.let {
                NavHostFragment.findNavController(it)
                    .navigate(ScanResultFragmentDirections.actionScanResultFragmentToMainFragment())
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ScanResultFragmentBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(requireActivity()).get(ScannerViewModel::class.java)
        binding.apply {
            honeyNameTxt.text = viewModel.honey.value?.name
            honeyDescTxt.text = viewModel.honey.value?.description
            firmTxt.text = viewModel.honey.value?.firmName
            if (viewModel.honey.value?.site != null) {
                siteBtn.setOnClickListener { _ ->
                    // Creo l'intent per andare su internet
                }
            }

            callBtn.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(viewModel.honey.value?.telephoneNumber)
                }
            }
            // Location intent which opens a map app with the address given
            locationBtn.setOnClickListener {
                val geo = Uri.parse(
                    "geo:0,0?q=" + Uri.encode(viewModel.honey.value?.address) + Uri.encode(", ") + Uri.encode(
                        viewModel.honey.value?.city
                    )
                )
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = geo
//                    `package` = "com.google.android.apps.maps"
                }
                startActivity(intent)
            }
        }
        return binding.root
    }
}
