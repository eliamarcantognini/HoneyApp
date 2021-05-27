package com.eliamarcantognini.honeyapp

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.games.Player
import com.google.android.gms.games.PlayerLevel

class AccountViewModel : ViewModel() {

    private var _account = MutableLiveData<GoogleSignInAccount>()
    val account: LiveData<GoogleSignInAccount>
        get() = _account

    private var _player = MutableLiveData<Player>()
    val player: LiveData<Player>
        get() = _player

    val name: String?
        get() = _player.value?.name


    val displayName: String?
        get() = _player.value?.displayName


    val imageUri: Uri?
        get() = _player.value?.hiResImageUri

    val playerLevel: PlayerLevel?
        get() = _player.value?.levelInfo?.currentLevel


//    init {
//        _account.value
//    }

    fun updatePlayer(signedPlayer: Player) {
        _player.value = signedPlayer
    }

    fun updateAccount(signedAccount: GoogleSignInAccount) {
        _account.value = signedAccount
    }


}