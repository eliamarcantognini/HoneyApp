package com.eliamarcantognini.honeyapp.ui.home

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.eliamarcantognini.honeyapp.R
import com.eliamarcantognini.honeyapp.databinding.HomeFragmentBinding
import com.eliamarcantognini.honeyapp.ui.menu.scanner.ScannerFragment

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel
    private var _binding : HomeFragmentBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = HomeFragmentBinding.inflate(inflater, container, false)
        val root : View = binding.root

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        // TODO: Use the ViewModel

        val navController = requireView().findNavController()

        binding.scanBtn.setOnClickListener {
            navController.navigate(HomeFragmentDirections.actionMainFragmentToScanboardFragment())
        }

        binding.statusCard.setOnClickListener {
            val action = HomeFragmentDirections.actionMainFragmentToStatusFragment2()
            navController.navigate(action)
        }

        binding.leaderboardCard.setOnClickListener {
            val action = HomeFragmentDirections.actionMainFragmentToLeaderboardFragment()
            navController.navigate(action)
        }

        binding.scanboardCard.setOnClickListener {
            val action = HomeFragmentDirections.actionMainFragmentToScanboardFragment()
            navController.navigate(HomeFragmentDirections.actionMainFragmentToScanboardFragment())
        }
    }
}