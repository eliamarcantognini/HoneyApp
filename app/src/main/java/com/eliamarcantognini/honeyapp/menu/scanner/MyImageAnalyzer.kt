package com.eliamarcantognini.honeyapp.menu.scanner

import android.annotation.SuppressLint
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
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
                    val honey = Json.decodeFromString<Honey>(Honey.serializer(), json)
                    scannerViewModel.update(honey)
//                    scannerViewModel.onScanResult()

                }
            }
        }
    }
}