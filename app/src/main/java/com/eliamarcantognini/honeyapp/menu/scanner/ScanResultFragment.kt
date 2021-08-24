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
import com.eliamarcantognini.honeyapp.R
import com.eliamarcantognini.honeyapp.databinding.ScanResultFragmentBinding
import com.eliamarcantognini.honeyapp.login.AccountViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.games.Games
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class ScanResultFragment : Fragment() {

    private lateinit var viewModel: ScannerViewModel
    private var _binding: ScanResultFragmentBinding? = null
    private lateinit var accountViewModel: AccountViewModel

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
    ): View {
        _binding = ScanResultFragmentBinding.inflate(inflater, container, false)

        val activity = requireActivity()
        viewModel = ViewModelProvider(activity).get(ScannerViewModel::class.java)
        accountViewModel = ViewModelProvider(activity).get(AccountViewModel::class.java)
        binding.apply {
            when (viewModel.honey.value?.type) {
                0 -> honeyNameTxt.text = getString(R.string.millefiori)
                1 -> honeyNameTxt.text = getString(R.string.castagno)
                2 -> honeyNameTxt.text = getString(R.string.acacia)
                3 -> honeyNameTxt.text = getString(R.string.eucalipto)
                4 -> honeyNameTxt.text = getString(R.string.girasole)
                5 -> honeyNameTxt.text = getString(R.string.agrumi)
                6 -> honeyNameTxt.text = getString(R.string.timo)
                7 -> honeyNameTxt.text = getString(R.string.tiglio)
                8 -> honeyNameTxt.text = getString(R.string.melata)
                9 -> honeyNameTxt.text = getString(R.string.sulla)
                10 -> honeyNameTxt.text = getString(R.string.altromiele)
            }
            honeyDescTxt.text = viewModel.honey.value?.description
            firmTxt.text = viewModel.honey.value?.firmName
            // If the site is given, it renders the button to open the browser with the url given
            viewModel.honey.value?.site?.let {
                siteBtn.visibility = View.VISIBLE
                siteBtn.setOnClickListener { _ ->
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https:$it")))
                }
            }
            // Dial intent which opens the dialer with the given telephone number
            callBtn.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:" + viewModel.honey.value?.telephoneNumber)
                }
                startActivity(intent)
            }
            // Location intent which opens a map app with the address given
            locationBtn.setOnClickListener {
                val geo = Uri.parse(
                    "geo:0,0?q=" + Uri.encode(viewModel.honey.value?.address) + Uri.encode(", ") + Uri.encode(
                        viewModel.honey.value?.city
                    )
                )
                startActivity(Intent(Intent.ACTION_VIEW, geo))
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Update games stats
        context.let {
            val account = GoogleSignIn.getLastSignedInAccount(it)
            val gamesClient = Games.getGamesClient(it, account!!)
            view.let {
                gamesClient.setViewForPopups(view.findViewById(R.id.info_popup))
//                gamesClient.setGravityForPopups(Gravity.TOP or Gravity.CENTER_HORIZONTAL)
            }
            // achievement
            // TODO. FIX POPUP NOT SHOWING
            Games.getAchievementsClient(it, account)
                .unlock(getString(R.string.achievement_benvenuto_tra_le_api))
            Games.getAchievementsClient(it, account)
                .increment(getString(R.string.achievement_stai_diventando_forte), 1);
            Games.getAchievementsClient(it, account)
                .increment(getString(R.string.achievement_continua_cos), 1);
            // leaderboard
//            Games.getLeaderboardsClient(it, GoogleSignIn.getLastSignedInAccount(it))
//                .loadCurrentPlayerLeaderboardScore(getString(R.string.leaderboard_scannerizzazioni), 1, 1).addOnSuccessListener {
//                }

            Games.getLeaderboardsClient(it, account)
                .submitScore(getString(R.string.leaderboard_scannerizzazioni), 1);
        }
    }
}
