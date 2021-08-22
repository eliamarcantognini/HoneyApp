package com.eliamarcantognini.honeyapp.login

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

sealed class FirebaseAuthUserState
data class UserSignedIn(val user: FirebaseUser) : FirebaseAuthUserState()
object UserSignedOut : FirebaseAuthUserState()
object UserUnknown : FirebaseAuthUserState()

@MainThread
fun FirebaseAuth.newFirebaseAuthStateLiveData(
    context: CoroutineContext = EmptyCoroutineContext
): LiveData<FirebaseAuthUserState> {
    val ld = FirebaseAuthStateLiveData(this)
    return liveData(context) {
        emitSource(ld)
    }
}
