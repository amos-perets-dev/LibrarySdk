package com.example.mylibrary.library.moduels

import android.content.Context
import android.util.Size
import androidx.camera.core.ImageAnalysis
import androidx.camera.lifecycle.ProcessCameraProvider
import com.example.mylibrary.library.init.QrCodeScanConfiguration
import com.example.mylibrary.network.base.BaseNetworkManager
import com.example.mylibrary.network.base.IBaseNetworkManager
import com.example.mylibrary.network.error.HandleNetworkError
import com.example.mylibrary.network.error.IHandleNetworkError
import com.example.mylibrary.utils.analyzer.IImageAnalyzer
import com.example.mylibrary.utils.analyzer.ImageAnalyzer
import com.example.mylibrary.repo.ISoundsRepository
import com.example.mylibrary.repo.SoundsRepository
import com.example.mylibrary.screens.host.HostViewModel
import com.example.mylibrary.screens.sounds.SoundsViewModel
import com.example.mylibrary.screens.sounds.list.SoundsAdapter
import com.example.mylibrary.screens.sounds.manager.SoundsManager
import com.example.mylibrary.screens.sounds.manager.ISoundsManager
import com.example.mylibrary.utils.scanner.IQrCodeScanner
import com.example.mylibrary.utils.scanner.QrCodeScanner
import com.example.mylibrary.screens.welcome.WelcomeViewModel
import com.example.mylibrary.utils.files.FileUtil
import com.example.mylibrary.utils.files.IFileUtil
import com.example.mylibrary.utils.media.IMediaPlayerHelper
import com.example.mylibrary.utils.media.MediaPlayerHelper
import com.example.mylibrary.utils.permissions.IPermissionsUtil
import com.example.mylibrary.utils.permissions.PermissionsUtil
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Create modules of the APP
 */
class QrLibraryModules {

    fun createModules(context: Context): List<Module> {

        val applicationModule = createApplicationModule(context)
        val moduleSounds = createSoundsModule()
        val moduleWelcome = createWelcomeModule()
        val moduleHost = createHostModule()

        return listOf(
            applicationModule,
            moduleWelcome,
            moduleSounds,
            moduleHost
        )

    }

    private fun createWelcomeModule(): Module {
        return module {

            viewModel {
                WelcomeViewModel()
            }
        }
    }

    private fun createHostModule(): Module {
        return module {

            viewModel {
                HostViewModel()
            }
        }
    }

    private fun createSoundsModule(): Module {
        return module {

            factory { SoundsAdapter() }

            single<ISoundsManager> {
                SoundsManager(
                    get(), get()
                )
            }

            viewModel {
                SoundsViewModel(get(), get(), get(), QrCodeScanConfiguration.create())
            }

        }
    }

    private fun createApplicationModule(context: Context): Module {
        val imageAnalysis = createImageAnalysis()
        return module {
            factory<IFileUtil> { FileUtil() }
            factory<IPermissionsUtil> { PermissionsUtil() }
            single<IBaseNetworkManager> { BaseNetworkManager() }
            single<ISoundsRepository> { SoundsRepository(get()) }
            factory<IHandleNetworkError> { HandleNetworkError() }
            factory<IMediaPlayerHelper> { MediaPlayerHelper(context, get()) }
            factory<IQrCodeScanner> {
                QrCodeScanner(
                    (ImageAnalyzer() as IImageAnalyzer),
                    ProcessCameraProvider.getInstance(context),
                    context,
                    imageAnalysis
                )
            }
        }
    }

    private fun createImageAnalysis(): ImageAnalysis {
        return ImageAnalysis.Builder()
            .setTargetResolution(Size(1280, 720))
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
    }
}