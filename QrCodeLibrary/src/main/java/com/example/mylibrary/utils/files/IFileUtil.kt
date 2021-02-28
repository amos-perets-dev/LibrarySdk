package com.example.mylibrary.utils.files

import android.net.Uri

interface IFileUtil {

    /**
     * Call to generate the [ByteArray](of the sound) to [Uri]
     */
    fun generateUri(bytes: ByteArray?): Uri?
}