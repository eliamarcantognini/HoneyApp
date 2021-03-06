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
import com.eliamarcantognini.honeyapp.R
import com.eliamarcantognini.honeyapp.databinding.ScanboardFragmentBinding
import com.eliamarcantognini.honeyapp.firestore.Scan
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.util.*

class ScanboardFragment : Fragment(), OnScanListener {

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
        viewModel.scan.observe(requireActivity(), {
            Log.d("PROVA", "osservo2");
            binding.apply {
                progressBar.progressBar.visibility = View.VISIBLE
            }
            loadScans()
        })
        viewModel.stars.observe(requireActivity(), {
            Log.d("PROVA", "osservo");
            binding.apply {
                progressBar.progressBar.visibility = View.VISIBLE
            }
            loadScans()
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = NavHostFragment.findNavController(this)
        binding.apply {
            progressBar.progressBar.visibility = View.VISIBLE
            fabLayout.fabScan.setOnClickListener {
                navController.navigate(ScanboardFragmentDirections.actionScanboardFragmentToScannerFragment())
            }
            recyclerView.setHasFixedSize(true)
            adapter = HoneyScanAdapter(this@ScanboardFragment)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
        }
        loadScans()
    }


    fun loadScans() {
        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser!!.uid

        val scansRef = db.collection("scans").document(userId).collection("data")
            .orderBy("time", Query.Direction.DESCENDING)
        scansRef.get().addOnSuccessListener {
            val data: ArrayList<Scan> = arrayListOf()
            for (doc in it.documents) {
                doc.toObject(Scan::class.java)?.let { it1 ->
                    data.add(it1)
                    when (it1.type) {
                        "Miele Millefiori" -> it1.resID = R.drawable.millefiori
                        "Miele di Castagno" -> it1.resID = R.drawable.castagno
                        "Miele di Acacia" -> it1.resID = R.drawable.ic_acacia
                        "Miele di Eucalipto" -> it1.resID = R.drawable.eucalipto
                        "Miele di Girasole" -> it1.resID = R.drawable.girasole
                        "Miele di Agrumi" -> it1.resID = R.drawable.agrumi
                        "Miele di Timo" -> it1.resID = R.drawable.timo
                        "Miele di Tiglio" -> it1.resID = R.drawable.tiglio
                        "Miele di Melata" -> it1.resID = R.drawable.melata
                        "Miele di Sulla" -> it1.resID = R.drawable.sulla
                        "Miele molto raro" -> it1.resID = R.drawable.ic_altro
                    }
                }
            }
            adapter.setData(data)
            binding.apply { progressBar.progressBar.visibility = View.GONE }
        }
    }

    override fun onScanClick(position: Int) {
        binding.apply {
            val holder = recyclerView.findViewHolderForAdapterPosition(position) as HoneyScanHolder
            val scan = holder.scan
            viewModel.updateScan(scan)
            navController.navigate(ScanboardFragmentDirections.actionScanboardFragmentToHoneyInfoDialog())
        }
    }

}