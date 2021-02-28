package com.example.mylibrary.utils.analyzer

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import io.reactivex.Single

interface IImageAnalyzer : ImageAnalysis.Analyzer {

    /**
     * Get the data from the scanner
     */
    fun qrCodeScanner(): Single<String>?

    /**
     *
     */
    override fun analyze(image: ImageProxy)
}