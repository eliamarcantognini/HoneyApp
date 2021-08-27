package com.eliamarcantognini.honeyapp.menu.scanner

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ScannerViewModel : ViewModel() {

    private val _honey = MutableLiveData<Honey>()
    val honey: LiveData<Honey>
        get() = _honey

    private val _eventScanResult = MutableLiveData<Boolean>()
    val eventScanResult: LiveData<Boolean>
        get() = _eventScanResult

    init {
        val initHoney = Honey("", "", "", "", "", "", "", 1, "")
        _honey.value = initHoney
        _eventScanResult.value = false
    }

    fun update(scanned: Honey) {
        _honey.value = scanned
        onScanResult()
    }

    private fun onScanResult() {
        _eventScanResult.value = true
    }

    fun onScanResultComplete() {
        _eventScanResult.value = false
    }

}