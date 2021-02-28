package com.example.mylibrary.screens.welcome

import androidx.lifecycle.ViewModel
import com.example.mylibrary.R
import com.example.mylibrary.utils.SingleLiveEvent
import io.reactivex.disposables.CompositeDisposable

class WelcomeViewModel : ViewModel(){

    val nextPage = SingleLiveEvent<Int>()
    val permissionNeeded = SingleLiveEvent<String>()

    private val compositeDisposable = CompositeDisposable()

    /**
     * Call when the user click on start scan
     */
    fun onClickStartScan() {
        permissionNeeded.postValue(android.Manifest.permission.CAMERA)
    }

    fun onClickPermissionRequest(isPermissionGranted : Boolean){
        if (isPermissionGranted.not()) return
        nextPage.postValue(R.id.action_welcomeFragment_to_soundsFragment)
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

}