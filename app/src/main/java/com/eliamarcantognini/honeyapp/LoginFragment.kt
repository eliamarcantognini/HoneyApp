package com.eliamarcantognini.honeyapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.eliamarcantognini.honeyapp.databinding.LoginFragmentBinding
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.drive.Drive
import com.google.android.gms.games.Games
import com.google.android.gms.games.GamesClient
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class LoginFragment : Fragment() {

    private var _binding: LoginFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var googleSignInClient: GoogleSignInClient

//    private lateinit var googleSignInOptions: GoogleSignInOptions
//    private lateinit var googleSignInAccount: GoogleSignInAccount
    private lateinit var gamesClient: GamesClient

    //    private lateinit var oneTapClient: SignInClient
//    private lateinit var signInRequest: BeginSignInRequest
    private lateinit var navController: NavController
    private lateinit var viewModel: AccountViewModel
    private var RC_SIGN_IN = 101

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LoginFragmentBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = requireContext()
        navController = NavHostFragment.findNavController(this)

        viewModel = ViewModelProvider(requireActivity()).get(AccountViewModel::class.java)
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
            .requestScopes(Drive.SCOPE_APPFOLDER)
            .build()
        googleSignInClient =
            GoogleSignIn.getClient(requireContext(), googleSignInOptions)
        binding.apply {
            btnSignIn.setOnClickListener {
                val signInIntent = googleSignInClient.signInIntent
                requireActivity().startActivityFromFragment(this@LoginFragment, signInIntent, RC_SIGN_IN)
            }
        }
        signInSilently()


    }

    private fun signInSilently() {
        val activity = requireActivity()
        val context = requireContext()
        val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
            .requestScopes(Drive.SCOPE_APPFOLDER).build()
        val account = GoogleSignIn.getLastSignedInAccount(context)
        if (GoogleSignIn.hasPermissions(account, *signInOptions.scopeArray)) {
            // Already signed in.
            // The signed in account is stored in the 'account' variable.
            updatePlayerInformation(context, account!!)
        } else {
            // Haven't been signed-in before. Try the silent sign-in first.
            val signInClient = GoogleSignIn.getClient(activity, signInOptions)
            signInClient
                .silentSignIn()
                .addOnCompleteListener(
                    activity
                ) { task ->
                    if (task.isSuccessful) {
                        // The signed in account is stored in the task's result.
                        val signedInAccount: GoogleSignInAccount = task.result
                        updatePlayerInformation(context, signedInAccount)
                    } else {
                        // Player will need to sign-in explicitly using via UI.
                        // See [sign-in best practices](http://developers.google.com/games/services/checklist) for guidance on how and when to implement Interactive Sign-in,
                        // and [Performing Interactive Sign-in](http://developers.google.com/games/services/android/signin#performing_interactive_sign-in) for details on how to implement
                        // Interactive Sign-in.
                        val intent = signInClient.signInIntent
                        activity.startActivityFromFragment(this, intent, RC_SIGN_IN)

                    }
                }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result!!.isSuccess) {
                // The signed in account is stored in the result.
                val signedInAccount = result.signInAccount
                signedInAccount?.let { updatePlayerInformation(requireContext(), it) }
            } else {
                var message = result.status.statusMessage
                if (message == null || message.isEmpty()) {
                    message = "Accedi per proseguire"
                }
//                AlertDialog.Builder(requireContext()).setMessage(message)
//                    .setNeutralButton(R.string.ok, null).show()
                MaterialAlertDialogBuilder(requireContext()).setMessage(message).setNeutralButton("OK", null).show()
            }
        }
    }

    private fun updatePlayerInformation(context: Context, signedAccount: GoogleSignInAccount) {
        val gamesClient = Games.getGamesClient(context, signedAccount)
        gamesClient.setViewForPopups(requireView())
//        gamesClient.setGravityForPopups(Gravity.TOP or Gravity.CENTER_HORIZONTAL)
        val playerClient = Games.getPlayersClient(context, signedAccount)
        val task = playerClient.currentPlayer
        task.addOnSuccessListener {
            viewModel.updateAccount(signedAccount)
            viewModel.updatePlayer(it)
            navController.navigate(LoginFragmentDirections.actionSplashFragmentToMainFragment())
        }

    }
}
