package com.eliamarcantognini.honeyapp.menu.scanboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eliamarcantognini.honeyapp.firestore.Scan
import com.eliamarcantognini.honeyapp.menu.scanner.Honey


class ScanboardViewModel : ViewModel() {

    private val _honey = MutableLiveData<Scan>()
    val honey: LiveData<Scan>
        get() = _honey

    init {
        val initHoney = Scan("", "", "", "", "", "", "", "")
        _honey.value = initHoney
    }


    fun update(scanned: Scan) {
        Log.d("SCANN", "HEHEHE")
        _honey.value = scanned
    }
}