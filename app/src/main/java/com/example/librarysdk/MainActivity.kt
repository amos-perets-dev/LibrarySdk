package com.example.librarysdk

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mylibrary.library.init.QrCodeScan

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        QrCodeScan.init(this)

    }

}
