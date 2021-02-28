package com.example.mylibrary.utils.analyzer

import android.annotation.SuppressLint
import android.util.Log
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject

class ImageAnalyzer : IImageAnalyzer {

    private val barcode = PublishSubject.create<String>()

    @Synchronized
    override fun analyze(image: ImageProxy) {
        scanBarcodeAsync(image)
    }

    override fun qrCodeScanner(): Single<String>? {
        return barcode.hide()
            .filter { it.isNotEmpty() }
            .firstOrError()
    }

    @SuppressLint("UnsafeExperimentalUsageError")
    private fun scanBarcodeAsync(imageProxy: ImageProxy) {

        val image = imageProxy.image ?: return

        val inputImage = InputImage.fromMediaImage(image, imageProxy.imageInfo.rotationDegrees)
        val scanner = BarcodeScanning.getClient()

        scanner.process(inputImage)
            .addOnCompleteListener {

                if (it.isSuccessful) {
                    val barcodes = it.result as List<Barcode>
                    val barcodeData = readBarcodeData(barcodes)
                    barcode.onNext(barcodeData ?: "")
                    imageProxy.close()

                } else {
                    it.exception?.printStackTrace()
                }
            }
    }

    private fun readBarcodeData(barcodes: List<Barcode>): String? {

        for (barcode in barcodes) {

            when (barcode.valueType) {
                Barcode.TYPE_URL -> {
                    return barcode.url?.url.toString()
                }
            }
        }
        return null
    }
}