package com.example.mylibrary.network.error

import androidx.annotation.StringRes
import com.example.mylibrary.R
import java.net.ConnectException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException
import javax.net.ssl.SSLHandshakeException
import javax.net.ssl.SSLProtocolException

class HandleNetworkError : IHandleNetworkError {

    @StringRes
    override fun generateErrorID(it: Throwable): Int {
        if (it is UnknownHostException
            || it is SSLHandshakeException
            || it is TimeoutException
            || it is ConnectException
            || it is SSLProtocolException) {
            return R.string.sounds_screen_empty_internet_error_text

        }
        return R.string.sounds_screen_empty_general_error_text
    }
}