package com.example.mylibrary.repo

import android.net.Uri
import android.util.Log
import com.example.mylibrary.utils.files.IFileUtil
import okhttp3.ResponseBody
import retrofit2.Response

class SoundsRepository(private val fileUtil : IFileUtil) : ISoundsRepository{

    private val soundsData = hashMapOf<String, Uri?>()

    override fun addSound(response: Response<ResponseBody?>?, link: String) {
        val bytes = response?.body()?.bytes()

        val uri = fileUtil.generateUri(bytes)

        soundsData[link] = uri

    }

    override fun getSound(link: String): Uri?  = soundsData[link]


}