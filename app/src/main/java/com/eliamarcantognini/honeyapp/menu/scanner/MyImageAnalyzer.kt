package com.eliamarcantognini.honeyapp.menu.scanner

import android.annotation.SuppressLint
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import kotlinx.serialization.json.Json

class MyImageAnalyzer(
    private var fragmentActivity: FragmentActivity,
) : ImageAnalysis.Analyzer {

    private lateinit var scannerViewModel: ScannerViewModel

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
                    val json = barcode.displayValue
                    val honey = Json.decodeFromString(Honey.serializer(), json)
                    scannerViewModel.update(honey)
                    updateDatabase(honey)
                    scannerViewModel.onScanComplete()
                }
            }
            if (scannerViewModel.scanComplete.value!!)
                break
        }
    }

    private fun updateDatabase(honey: Honey) {
        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser!!.uid

        val scansRef = db.collection("scans").document(userId)
        scansRef.get().addOnSuccessListener {
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
                "city" to honey.city,
                "cap" to honey.cap,
                "site" to honey.site,
                "num" to honey.telephoneNumber
            )
            if (!it.exists()) {
                scansRef.set(newScan)
            } else {
                scansRef.update("data", FieldValue.arrayUnion(newScan))
            }
        }

        val userRef = db.collection("users").document(userId)
        userRef.update("points", FieldValue.increment(20))
        userRef.update("scan", FieldValue.increment(1))
        userRef.get().addOnCompleteListener() { it1 ->
            if (it1.isSuccessful) {
                val points = it1.result["points"].toString().toInt()
                if (points == 100 || points == 220 || points == 360 || points == 580 || points == 800 || points == 1000) {
                    userRef.update("level", FieldValue.increment(1))
                }
            }
        }
    }
}