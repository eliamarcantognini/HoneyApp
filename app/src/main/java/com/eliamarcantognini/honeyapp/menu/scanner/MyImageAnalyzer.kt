package com.eliamarcantognini.honeyapp.menu.scanner

import android.annotation.SuppressLint
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.eliamarcantognini.honeyapp.firestore.User
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ServerTimestamp
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.util.*

class MyImageAnalyzer(
    private var fragmentActivity: FragmentActivity,
) : ImageAnalysis.Analyzer {

    private lateinit var scannerViewModel: ScannerViewModel
    private var dialog = false
    private var scanned = false

//    @ServerTimestamp
//    private val timestamp: Date? = null

    override fun analyze(image: ImageProxy) {
        scanBarcode(image)
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun scanBarcode(imageProxy: ImageProxy) {
        imageProxy.image?.let { image ->
            val options =
                BarcodeScannerOptions.Builder().setBarcodeFormats(Barcode.FORMAT_QR_CODE).build()
            val inputImage = InputImage.fromMediaImage(image, imageProxy.imageInfo.rotationDegrees)
            val scanner = BarcodeScanning.getClient(options)
            scanner.process(inputImage)
                .addOnCompleteListener {
                    imageProxy.close()
                    if (it.isSuccessful) {
                        readBarcodeData(it.result as List<Barcode>)
                    } else {
                        it.exception?.printStackTrace()
                    }
                }
        }
    }

    private fun readBarcodeData(barcodes: List<Barcode>) {
        scannerViewModel = ViewModelProvider(fragmentActivity).get(ScannerViewModel::class.java)
        for (barcode in barcodes) {
            when (barcode.valueType) {
                Barcode.TYPE_TEXT -> {
                    val json = barcode.displayValue!!
                    if (isJSONValid(json)) {
                        if (!scanned) {
                            scanned = true
                            val honey = Json.decodeFromString(Honey.serializer(), json)
                            scannerViewModel.update(honey)
                            updateDatabase()
                        }
                    } else {
                        if (!dialog) {
                            showDialog()
                        }
                    }
                }
            }
        }
    }

    private fun updateDatabase() {
        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser!!.uid
        val honey = scannerViewModel.honey.value!!
        val token = honey.token!!
        val scansRef = db.collection("scans").document(userId).collection("data").document(token)
        scansRef.get().addOnSuccessListener {
            if (!it.exists()) {
                var honeyType = ""
                when (honey.type) {
                    0 -> honeyType = "Miele Millefiori"
                    1 -> honeyType = "Miele di Castagno"
                    2 -> honeyType = "Miele di Acacia"
                    3 -> honeyType = "Miele di Eucalipto"
                    4 -> honeyType = "Miele di Girasole"
                    5 -> honeyType = "Miele di Agrumi"
                    6 -> honeyType = "Miele di Timo"
                    7 -> honeyType = "Miele di Tiglio"
                    8 -> honeyType = "Miele di Melata"
                    9 -> honeyType = "Miele di Sulla"
                    10 -> honeyType = "Miele molto raro"
                }
                val newScan = hashMapOf(
                    "type" to honeyType,
                    "firm" to honey.firmName,
                    "desc" to honey.description,
                    "addr" to honey.address,
                    "token" to token,
                    "city" to honey.city,
                    "cap" to honey.cap,
                    "site" to honey.site,
                    "num" to honey.telephoneNumber,
                    "time" to FieldValue.serverTimestamp(),
                    "stars" to 0
                )
                scansRef.set(newScan)
                val userRef = db.collection("users").document(userId)
                userRef.update("points", FieldValue.increment(20))
                userRef.update("scan", FieldValue.increment(1))
                userRef.get().addOnSuccessListener { it1 ->
                    val points = it1.toObject(User::class.java)!!.points!!.toInt()
                    if (points == 100 || points == 220 || points == 360 || points == 580 || points == 800 || points == 1000) {
                        userRef.update("level", FieldValue.increment(1))
                    }

                }
            }
        }
    }

    private fun isJSONValid(json: String): Boolean {
        try {
            Json.decodeFromString(Honey.serializer(), json)
            return true
        } catch (ex: SerializationException) {
            return false
        }
    }

    private fun showDialog() {
        val builder = MaterialAlertDialogBuilder(fragmentActivity)
        builder
            .setTitle("QR code non valido")
            .setNeutralButton("OK") { dialog, id -> dialog.cancel() }
            .show()
        dialog = true
    }


}