package com.eliamarcantognini.honeyapp.login

import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseAuth

class FirebaseAuthStateLiveData(private val auth: FirebaseAuth) :
    LiveData<FirebaseAuthUserState>() {
    private val authStateListener = MyAuthStateListener()

    init {
        value = UserUnknown
    }

    override fun onActive() {
        auth.addAuthStateListener(authStateListener)
    }

    override fun onInactive() {
        auth.removeAuthStateListener(authStateListener)
    }

    private inner class MyAuthStateListener : FirebaseAuth.AuthStateListener {
        override fun onAuthStateChanged(auth: FirebaseAuth) {
            val user = auth.currentUser
            value = if (user != null) {
                UserSignedIn(user)
            } else {
                UserSignedOut
            }
        }
    }
}