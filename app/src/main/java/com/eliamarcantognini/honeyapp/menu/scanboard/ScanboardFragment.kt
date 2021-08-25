package com.eliamarcantognini.honeyapp.menu.scanboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eliamarcantognini.honeyapp.R
import com.eliamarcantognini.honeyapp.databinding.ScanboardFragmentBinding
import com.eliamarcantognini.honeyapp.firestore.Scan
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.collections.ArrayList

class ScanboardFragment : Fragment(), OnScanListener {

    companion object {
        fun newInstance() = ScanboardFragment()
    }

    private lateinit var viewModel: ScanboardViewModel
    private var _binding: ScanboardFragmentBinding? = null
    private lateinit var layout: View
    private lateinit var adapter: HoneyScanAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var navController: NavController
    private lateinit var fab: FloatingActionButton

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
        initFab()
        initRecyclerView()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun initFab() {
        fab = requireActivity().findViewById(R.id.fab_scan)
        fab.setOnClickListener{
            navController.navigate(ScanboardFragmentDirections.actionScanboardFragmentToScannerFragment())
        }
    }

    private fun initRecyclerView() {
        recyclerView = requireActivity().findViewById(R.id.scanRecyclerView)
        recyclerView.setHasFixedSize(true)
        adapter = HoneyScanAdapter(this)
        recyclerView.adapter = adapter
        loadScans()
    }

    private fun loadScans() {
        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser!!.uid

        val scansRef = db.collection("scans").document(userId).collection("data")
        scansRef.get().addOnSuccessListener {
            val data : ArrayList<Scan> = arrayListOf()
            for (doc in it.documents) {

                doc.toObject(Scan::class.java)?.let { it1 -> data.add(it1) }
            }
//            Log.d("SCANLIST1", data.toString())
//
//            Log.d("SCANLIST", data.toString())
            adapter.setData(data)
        }

    }

    override fun onScanClick(position: Int) {
        val holder = recyclerView.findViewHolderForAdapterPosition(position) as HoneyScanHolder
        val honey = holder.honey
        viewModel.update(honey)
//        val name = (view.findViewById(R.id.honeyListName) as TextView).text
//        val descr = (view.findViewById(R.id.honeyListName) as TextView).text
        navController.navigate(ScanboardFragmentDirections.actionScanboardFragmentToHoneyInfoDialog())

    }

}