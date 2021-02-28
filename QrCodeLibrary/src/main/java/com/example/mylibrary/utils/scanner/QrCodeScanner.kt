package com.example.mylibrary.utils.scanner

import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.example.mylibrary.utils.analyzer.IImageAnalyzer
import com.google.common.util.concurrent.ListenableFuture
import io.reactivex.Single
import java.util.concurrent.Executors

class QrCodeScanner(
    private val analyzer: IImageAnalyzer,
    private val cameraProviderFuture: ListenableFuture<ProcessCameraProvider>,
    private val context: Context,
    private val imageAnalysis: ImageAnalysis
) : IQrCodeScanner {

    override fun getQrCodeAsync(): Single<String>? =
        analyzer.qrCodeScanner()
            ?.doOnEvent { t1, error ->
                if (error == null) {
                    stopSearch()
                }
            }

    override fun init(
        surfaceProvider: Preview.SurfaceProvider,
        viewLifecycleOwner: LifecycleOwner
    ) {
        cameraProviderFuture.addListener(Runnable {
            bindPreview(
                cameraProviderFuture.get(),
                surfaceProvider,
                viewLifecycleOwner
            )

        }, ContextCompat.getMainExecutor(context))

    }

    override fun stopSearch() {
        imageAnalysis.clearAnalyzer()
        cameraProviderFuture.cancel(true)
    }

    private fun bindPreview(
        cameraProvider: ProcessCameraProvider,
        surfaceProvider: Preview.SurfaceProvider,
        viewLifecycleOwner: LifecycleOwner
    ) {
        val preview: Preview = Preview.Builder()
            .build()
        val cameraSelector: CameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        preview.setSurfaceProvider(surfaceProvider)

        cameraProvider.bindToLifecycle(
            viewLifecycleOwner,
            cameraSelector,
            preview
        )

        imageAnalysis.setAnalyzer(Executors.newSingleThreadExecutor(), analyzer)

        cameraProvider.bindToLifecycle(
            viewLifecycleOwner,
            cameraSelector,
            imageAnalysis,
            preview
        )

    }

}