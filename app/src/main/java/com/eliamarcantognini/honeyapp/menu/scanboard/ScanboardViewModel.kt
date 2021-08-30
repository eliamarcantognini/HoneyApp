package com.eliamarcantognini.honeyapp.menu.scanboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eliamarcantognini.honeyapp.firestore.Scan
import com.eliamarcantognini.honeyapp.firestore.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class ScanboardViewModel : ViewModel() {

    private val _scan = MutableLiveData<Scan>()
    val scan: LiveData<Scan>
        get() = _scan

    private val _stars = MutableLiveData<Int>()
    val stars: LiveData<Int>
        get() = _stars

    init {
        val initScan = Scan("", "", "", "", "", "", "", "", 0, "")
        _scan.value = initScan
        _stars.value = 0
    }

    fun updateScan(scanned: Scan) {
        _scan.value = scanned
        _stars.value = scanned.stars!!
    }

    fun updateStars(number: Int) {
        _stars.value = number
    }

}