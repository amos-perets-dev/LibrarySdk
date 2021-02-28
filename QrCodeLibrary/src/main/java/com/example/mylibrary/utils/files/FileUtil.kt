package com.example.mylibrary.utils.files

import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class FileUtil : IFileUtil{
    override fun generateUri(bytes: ByteArray?): Uri? {
        val file = File.createTempFile("prefixTEMP","suffixTEMP");//creates temporary file

        return try {
            val fos = FileOutputStream(file)
            fos.write( bytes)

            Uri.fromFile(file)
        } catch (e : IOException) {
            e.printStackTrace()
            null
        }
    }

}