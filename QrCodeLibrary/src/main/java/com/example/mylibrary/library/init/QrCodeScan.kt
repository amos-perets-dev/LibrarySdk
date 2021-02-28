package com.example.mylibrary.library.init

import android.content.Context
import android.content.Intent
import com.example.mylibrary.screens.host.HostActivity

class QrCodeScan {

    /**
     * Call when the activity start
     */
    private fun startScan(context: Context?){
        context?.startActivity(Intent(context, HostActivity::class.java))
    }

    companion object {
        private var sInstance: QrCodeScan? = null

        @Synchronized
        private fun getInstance(): QrCodeScan? {
            if (sInstance == null) {
                sInstance =
                    QrCodeScan()
            }
            return sInstance
        }

        fun init(context: Context?){
            getInstance()
                ?.startScan(context)
        }
    }

}