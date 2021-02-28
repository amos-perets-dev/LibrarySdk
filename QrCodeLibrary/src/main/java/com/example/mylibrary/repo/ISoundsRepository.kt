package com.example.mylibrary.repo

import android.net.Uri
import okhttp3.ResponseBody
import retrofit2.Response

interface ISoundsRepository {

    /**
     * Call to store the audio files
     */
    fun addSound(response: Response<ResponseBody?>?, link: String)

    /**
     * Call when the user click on audio files from the main list
     */
    fun getSound(link: String): Uri?


}