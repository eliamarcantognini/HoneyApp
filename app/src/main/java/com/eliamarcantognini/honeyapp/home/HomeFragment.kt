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
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.eliamarcantognini.honeyapp.login.AccountViewModel
import com.eliamarcantognini.honeyapp.R
import com.eliamarcantognini.honeyapp.databinding.HomeFragmentBinding
import com.google.android.gms.common.images.ImageManager
import com.google.android.gms.games.Games
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PlayGamesAuthProvider

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: AccountViewModel
    private lateinit var layout: View
    private var _binding: HomeFragmentBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!
    private val RC_LEADERBOARD_UI = 102
    private val RC_ACHIEVEMENT_UI = 103

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // This callback will only be called when MyFragment is at least Started.
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            parentFragment?.let {
                requireActivity().finishAffinity()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HomeFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity()).get(AccountViewModel::class.java)
        val root: View = binding.root
        layout = binding.homeFragment

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

        binding.apply {
            scanBtn.setOnClickListener { requestPermissionLauncher.launch(Manifest.permission.CAMERA) }
            statusCard.setOnClickListener {
                Games.getAchievementsClient(activity, viewModel.account.value!!)
                    .achievementsIntent
                    .addOnSuccessListener {
                        activity.startActivityFromFragment(this@HomeFragment, it, RC_ACHIEVEMENT_UI)
                    }
            }
            leaderboardCard.setOnClickListener {
                Games.getLeaderboardsClient(activity, viewModel.account.value!!)
                    .getLeaderboardIntent(getString(R.string.leaderboard_scannerizzazioni))
                    .addOnSuccessListener {
                        activity.startActivityFromFragment(this@HomeFragment, it, RC_LEADERBOARD_UI)
                    }
            }
            scanboardCard.setOnClickListener { navController.navigate(HomeFragmentDirections.actionMainFragmentToScanboardFragment()) }
            Log.i("AIUTO2", "${viewModel.player.value?.name}")
            nameProfileTxt.text = viewModel.name
            locationProfileTxt.text = viewModel.displayName
            pointProfileTxt.text = viewModel.playerLevel?.levelNumber.toString()

            // Così accedo ai dati di un profilo playgames tramite firebase
            viewModel.firebaseUser.value?.let {
                Log.d("FIREBASEAA", "Ci arrivo qui?")
                for (profile in it.providerData) {
                    if (profile.providerId == PlayGamesAuthProvider.PROVIDER_ID)
                    {
                        Log.d("FIREBASEAA", "Non sembra")
                        scanProfileTxt.text = profile.displayName
                    }
                }
            }

            val imageManager = ImageManager.create(requireContext())
            imageManager.loadImage(profileImg, viewModel.imageUri)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_LEADERBOARD_UI) {

        }
    }

    private fun showDialog(activity: Activity) {
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