package com.eliamarcantognini.honeyapp.menu.scanboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.eliamarcantognini.honeyapp.databinding.ScanboardFragmentBinding
import com.eliamarcantognini.honeyapp.firestore.Scan
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.util.*
import kotlin.collections.ArrayList

class ScanboardFragment : Fragment(), OnScanListener {

    companion object {
        fun newInstance() = ScanboardFragment()
    }

    private lateinit var viewModel: ScanboardViewModel
    private var _binding: ScanboardFragmentBinding? = null
    private lateinit var adapter: HoneyScanAdapter
    private lateinit var navController: NavController

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ScanboardFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity()).get(ScanboardViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = NavHostFragment.findNavController(this)
        binding.apply {
            progressBar.progressBar.visibility = View.VISIBLE
            fabLayout.fabScan.setOnClickListener{
                navController.navigate(ScanboardFragmentDirections.actionScanboardFragmentToScannerFragment())
            }
            recyclerView.setHasFixedSize(true)
            adapter = HoneyScanAdapter(this@ScanboardFragment)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
        }
        loadScans()
    }


    private fun loadScans() {
        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser!!.uid

        val scansRef = db.collection("scans").document(userId).collection("data").orderBy("time", Query.Direction.DESCENDING)
        scansRef.get().addOnSuccessListener {
            val data : ArrayList<Scan> = arrayListOf()
            for (doc in it.documents) {
                doc.toObject(Scan::class.java)?.let { it1 -> data.add(it1); Log.d("AAA",
                    it1.stars!!.toString()
                )}
            }
            adapter.setData(data)
            binding.apply { progressBar.progressBar.visibility = View.GONE }
        }

    }

    override fun onScanClick(position: Int) {
        binding.apply {
            val holder = recyclerView.findViewHolderForAdapterPosition(position) as HoneyScanHolder
            val scan = holder.scan
            viewModel.update(scan)
            navController.navigate(ScanboardFragmentDirections.actionScanboardFragmentToHoneyInfoDialog())
        }

    }

}