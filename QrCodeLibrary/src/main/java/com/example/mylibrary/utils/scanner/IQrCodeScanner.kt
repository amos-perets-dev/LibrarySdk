package com.example.mylibrary.utils.scanner

import androidx.camera.core.Preview
import androidx.lifecycle.LifecycleOwner
import io.reactivex.Single

interface IQrCodeScanner {

    /**
     * Get the data from the scanner
     */
    fun getQrCodeAsync(): Single<String>?

    /**
     * Init the camera to scan the QrCode
     */
    fun init(
        surfaceProvider: Preview.SurfaceProvider,
        viewLifecycleOwner: LifecycleOwner
    )

    /**
     * Call when the page close or need to stope the sound
     */
    fun stopSearch()

}