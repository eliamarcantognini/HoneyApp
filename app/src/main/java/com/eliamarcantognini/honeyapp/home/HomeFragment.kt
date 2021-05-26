package com.eliamarcantognini.honeyapp.home

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.eliamarcantognini.honeyapp.databinding.HomeFragmentBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel
    private lateinit var layout: View
    private var _binding: HomeFragmentBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HomeFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root
        layout = binding.homeFragment

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        val activity = requireActivity()
        val navController = requireView().findNavController()

        val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    navController.navigate(HomeFragmentDirections.actionMainFragmentToScannerFragment())
                    Log.i("Permission: ", "Granted")
                } else {
                    showDialog(activity)
                    Log.i("Permission: ", "Denied")
                }
            }

        binding.scanBtn.setOnClickListener {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }

        binding.statusCard.setOnClickListener {
            val action = HomeFragmentDirections.actionMainFragmentToStatusFragment()
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

    fun showDialog(activity: Activity) {
        val builder = MaterialAlertDialogBuilder(activity)
        builder.setMessage("È necessario autorizzare l'accesso alla Fotocamera per scannerizzare il QR Code")
            .setTitle("Autorizzazione richiesta")
            .setPositiveButton("OK") { dialog, id ->
                Intent().also {
                    it.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    it.data = Uri.fromParts("package", activity.packageName, null)
                    startActivity(it)
                    activity.finish()
                }
            }
            .setNegativeButton("ANNULLA") { dialog, id -> dialog.cancel() }
            .setCancelable(false).show()
    }

}
//
//private fun View.showSnackbar(
//    view: View,
//    msg: String,
//    length: Int,
//    actionMessage: CharSequence?,
//    action: (View) -> Unit
//) {
//    val snackbar = Snackbar.make(view, msg, length)
//    if (actionMessage != null) {
//        snackbar.setAction(actionMessage) {
//            action(this)
//        }.show()
//    } else {
//        snackbar.show()
//    }
//}