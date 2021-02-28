package com.example.mylibrary.library.init

import androidx.annotation.ColorRes
import com.example.mylibrary.R

class QrCodeScanConfiguration(
    @ColorRes val emptyMsgColor: Int,
    @ColorRes val errorMsgColor: Int
) {


    companion object {
        private var sInstance: QrCodeScanConfiguration? = null

        @Synchronized
        fun create(
            emptyMsgColor: Int = R.color.sounds_screen_empty_color,
            errorMsgColor: Int = R.color.sounds_screen_error_color
        ): QrCodeScanConfiguration {
            if (sInstance == null) {
                sInstance =
                    QrCodeScanConfiguration(emptyMsgColor, errorMsgColor)
            }
            return sInstance as QrCodeScanConfiguration
        }

    }

    class Builder {

        @ColorRes
        private var errorMsgColor: Int = R.color.sounds_screen_empty_color

        @ColorRes
        private var emptyMsgColor: Int = R.color.sounds_screen_error_color

        fun setEmptyMsgColor( @ColorRes emptyMsgColor: Int) =
            apply { this.emptyMsgColor = emptyMsgColor }

        fun setErrorMsgColor( @ColorRes errorMsgColor: Int) =
            apply { this.errorMsgColor = errorMsgColor }


        fun build() {
            create(emptyMsgColor, errorMsgColor)
        }
    }


}