package com.eliamarcantognini.honeyapp.menu.scanboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eliamarcantognini.honeyapp.firestore.Scan


class ScanboardViewModel : ViewModel() {

    private val _scan = MutableLiveData<Scan>()
    val scan: LiveData<Scan>
        get() = _scan

    init {
        val initScan = Scan("", "", "", "", "", "", "", "", 0, "")
        _scan.value = initScan
    }


    fun update(scanned: Scan) {
        Log.d("SCANN", "HEHEHE")
        _scan.value = scanned
    }
}