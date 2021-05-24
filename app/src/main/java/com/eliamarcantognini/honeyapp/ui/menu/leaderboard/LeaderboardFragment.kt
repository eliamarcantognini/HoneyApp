package com.eliamarcantognini.honeyapp.ui.menu.leaderboard

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eliamarcantognini.honeyapp.R
import com.eliamarcantognini.honeyapp.databinding.HomeFragmentBinding
import com.eliamarcantognini.honeyapp.databinding.LeaderboardFragmentBinding

class LeaderboardFragment : Fragment() {

    private var _binding : LeaderboardFragmentBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!
    companion object {
        fun newInstance() = LeaderboardFragment()
    }

    private lateinit var viewModel: LeaderboardViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LeaderboardFragmentBinding.inflate(inflater, container, false)
        val root : View = binding.root

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LeaderboardViewModel::class.java)
        // TODO: Use the ViewModel
    }

}