package com.eliamarcantognini.honeyapp.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.eliamarcantognini.honeyapp.R
import com.eliamarcantognini.honeyapp.databinding.LoginFragmentBinding
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.drive.Drive
import com.google.android.gms.games.Games
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.*


class LoginFragment : Fragment() {

    private var _binding: LoginFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private lateinit var navController: NavController
    private lateinit var viewModel: AccountViewModel
    private var RC_SIGN_IN = 101
    private lateinit var signInOptions: GoogleSignInOptions

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LoginFragmentBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
            .requestServerAuthCode(getString(R.string.default_web_client_id))
//            .requestIdToken(getString(R.string.default_web_client_id))
//            .requestScopes(Drive.SCOPE_APPFOLDER)
            .build()
        auth = FirebaseAuth.getInstance()
        viewModel = ViewModelProvider(requireActivity()).get(AccountViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = requireContext()
        navController = NavHostFragment.findNavController(this)

        googleSignInClient =
            GoogleSignIn.getClient(requireContext(), signInOptions)
        binding.apply {
            progressBar.progressBar.visibility = View.VISIBLE
            btnSignIn.setOnClickListener {
                val signInIntent = googleSignInClient.signInIntent
                requireActivity().startActivityFromFragment(
                    this@LoginFragment,
                    signInIntent,
                    RC_SIGN_IN
                )
            }
        }
        auth = FirebaseAuth.getInstance()
        signInSilently()


    }

//    override fun onResume() {
//        super.onResume()
//        signInSilently()
//    }

    private fun signInSilently() {
        val activity = requireActivity()

        // Haven't been signed-in before. Try the silent sign-in first.
        val signInClient = GoogleSignIn.getClient(activity, signInOptions)
//        Log.d("FIREBASELOG", "Sono nel login vero")
        signInClient
            .silentSignIn()
            .addOnCompleteListener(
                activity
            ) { task ->
                if (task.isSuccessful) {
                    // The signed in account is stored in the task's result.
                    val signedInAccount: GoogleSignInAccount = task.result
                    firebaseAuthWithPlayGames(signedInAccount)
                } else {
                    val intent = signInClient.signInIntent
                    activity.startActivityFromFragment(this, intent, RC_SIGN_IN)
                }
            }
//        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result!!.isSuccess) {
                // The signed in account is stored in the result.
                val signedInAccount = result.signInAccount
                signedInAccount?.let {
                    firebaseAuthWithPlayGames(signedInAccount)
                }
            } else {
                var message = result.status.statusMessage
                if (message == null || message.isEmpty()) {
                    message = "Accedi per proseguire"
                }
                MaterialAlertDialogBuilder(requireContext()).setMessage(message)
                    .setNeutralButton("OK", null).show()
            }
        }
    }

    private fun firebaseAuthWithPlayGames(acct: GoogleSignInAccount) {
//        Log.d("FIREBASE", "firebaseAuthWithPlayGames:" + acct.id!!)

        auth = FirebaseAuth.getInstance()
        val credential = PlayGamesAuthProvider.getCredential(acct.serverAuthCode!!)
//        Log.d("FIREBASEPROV", credential.provider)
//
//        Log.d("FIREBASETOKEN", acct.serverAuthCode!!)

        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("FIREBASE", "signInWithCredential:success")
                    val user = auth.currentUser
                    updatePlayerInformation(requireContext(), acct, user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("FIREBASE", "signInWithCredential:failure", task.exception)
                    Toast.makeText(
                        requireContext(), "Firebase Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    updatePlayerInformation(requireContext(), acct, null)
                }
            }

    }

    private fun updatePlayerInformation(
        context: Context,
        signedAccount: GoogleSignInAccount,
        firebaseUser: FirebaseUser?
    ) {
        val gamesClient = Games.getGamesClient(context, signedAccount)
        gamesClient.setViewForPopups(requireView())
//        gamesClient.setGravityForPopups(Gravity.TOP or Gravity.CENTER_HORIZONTAL)
        val playerClient = Games.getPlayersClient(context, signedAccount)
        val task = playerClient.currentPlayer

        task.addOnSuccessListener {
            viewModel.updateAccount(signedAccount)
            viewModel.updatePlayer(it)
            viewModel.updateFirebaseUser(firebaseUser)
            navController.navigate(LoginFragmentDirections.actionSplashFragmentToMainFragment())
            binding.apply { progressBar.progressBar.visibility = View.GONE }
        }

    }

}

