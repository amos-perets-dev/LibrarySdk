package com.example.mylibrary.screens.host

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.example.mylibrary.R
import org.koin.android.ext.android.inject


class HostActivity : AppCompatActivity() {

    private val viewModel by inject<HostViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_host)

        Navigation.findNavController(
            this,
            R.id.nav_host_fragment
        ).navigate(R.id.welcomeFragment)

        viewModel.hostScreenState.observe(this, Observer (this::navigate))

    }

    /**
     * Navigate the the next page or exit
     */
    private fun navigate(isHost: Boolean) {
        if (isHost) {
            finish()
        } else {
            super.onBackPressed()
        }
    }

    override fun onBackPressed() {
        val currScreenId =
            Navigation.findNavController(this, R.id.nav_host_fragment).currentDestination
                ?.id ?: 0

        viewModel.onClickBackPress(currScreenId, R.id.welcomeFragment)
    }

}