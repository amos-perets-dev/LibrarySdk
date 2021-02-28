package com.example.mylibrary.screens.host

import androidx.lifecycle.ViewModel
import com.example.mylibrary.utils.SingleLiveEvent

class HostViewModel : ViewModel() {

    val hostScreenState = SingleLiveEvent<Boolean>()

    /**
     * Call when the user click on the back press button
     */
    fun onClickBackPress(currScreenId: Int, startScreenId: Int) {

        hostScreenState.postValue(currScreenId == startScreenId)

    }

}