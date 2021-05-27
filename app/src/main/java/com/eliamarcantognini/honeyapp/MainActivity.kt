package com.eliamarcantognini.honeyapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.eliamarcantognini.honeyapp.databinding.MainActivityBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions


class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN).requestId()
            .requestProfile().build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

    }

    override fun onResume() {
        super.onResume()
//        signInSilently()
    }
//
//    private fun signInSilently() {
//        val signInOptions = GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN
//        val account = GoogleSignIn.getLastSignedInAccount(this)
//        if (GoogleSignIn.hasPermissions(account, *signInOptions.scopeArray)) {
//            // Already signed in.
//            // The signed in account is stored in the 'account' variable.
//            val signedInAccount = account
//        } else {
//            // Haven't been signed-in before. Try the silent sign-in first.
//            val signInClient = GoogleSignIn.getClient(this, signInOptions)
//            signInClient
//                .silentSignIn()
//                .addOnCompleteListener(
//                    this
//                ) { task ->
//                    if (task.isSuccessful) {
//                        // The signed in account is stored in the task's result.
//                        val signedInAccount: GoogleSignInAccount = task.result
//                    } else {
//                        // Player will need to sign-in explicitly using via UI.
//                        // See [sign-in best practices](http://developers.google.com/games/services/checklist) for guidance on how and when to implement Interactive Sign-in,
//                        // and [Performing Interactive Sign-in](http://developers.google.com/games/services/android/signin#performing_interactive_sign-in) for details on how to implement
//                        // Interactive Sign-in.
//                    }
//                }
//        }
//    }

}