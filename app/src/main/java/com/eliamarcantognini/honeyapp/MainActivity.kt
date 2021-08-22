package com.eliamarcantognini.honeyapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.eliamarcantognini.honeyapp.databinding.MainActivityBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding
//    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN).requestId()
//            .requestProfile().build()
//        googleSignInClient = GoogleSignIn.getClient(this, gso)

        FirebaseAuth.getInstance().signOut()

    }

    override fun onDestroy() {
        super.onDestroy()
        FirebaseAuth.getInstance().signOut()
    }

}