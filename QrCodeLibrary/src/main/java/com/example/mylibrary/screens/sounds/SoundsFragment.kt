package com.example.mylibrary.screens.sounds

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.mylibrary.R
import com.example.mylibrary.screens.base.BaseFragment
import com.example.mylibrary.screens.sounds.list.SoundItemEvent
import com.example.mylibrary.screens.sounds.list.SoundsAdapter
import com.example.mylibrary.utils.scanner.IQrCodeScanner
import kotlinx.android.synthetic.main.fragment_sounds.view.*
import org.koin.android.ext.android.inject

class SoundsFragment : BaseFragment() {

    private val viewModel by inject<SoundsViewModel>()
    private val qrCodeScanner by inject<IQrCodeScanner>()
    private val soundsAdapter by inject<SoundsAdapter>()

    override fun getLayout(): Int = R.layout.fragment_sounds

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.dataResult.observe(viewLifecycleOwner, Observer { state ->

            if (state is SoundsViewModel.DataResult.Loading) {
                showLoading(view)
            } else {
                view.progress_loading.animate().alpha(0F).setDuration(300).start()

                when (state) {
                    is SoundsViewModel.DataResult.Success -> {
                        showList(view, state.data)
                    }
                    is SoundsViewModel.DataResult.Msg -> {
                        showMsg(view, state)
                    }
                }
            }
        })

        qrCodeScanner.init(view.preview_view.surfaceProvider, viewLifecycleOwner)

        qrCodeScanner
            .getQrCodeAsync()
            ?.subscribe(viewModel::qrCodeDataChange)?.let {
                compositeDisposable.add(
                    it

                )
            }
    }

    /**
     * Show loading state
     */
    private fun showLoading(
        view: View
    ) {
        view.preview_view.animate().alpha(0F).setDuration(200).start()
        view.progress_loading.animate().alpha(1F).setDuration(200).start()
    }

    /**
     * Show error / empty msg [SoundsViewModel.DataResult]
     */
    private fun showMsg(
        view: View,
        msg: SoundsViewModel.DataResult.Msg
    ) {
        val error = view.error
        error.text = getString(msg.message)
        error.setTextColor(ContextCompat.getColor(requireContext(), msg.messageColor))
        error.animate().alpha(1F).setDuration(200).start()

    }

    /**
     * Show the sounds list
     */
    private fun showList(
        view: View,
        data: List<SoundItemEvent?>?
    ) {
        view.title.animate().alpha(1F).setDuration(200).start()
        soundsAdapter.setData(data)
        val recyclerViewSoundsList = view.recycler_view_sounds_list
        recyclerViewSoundsList.adapter = soundsAdapter
        recyclerViewSoundsList.animate().alpha(1F).setDuration(150).start()

    }

    override fun onDestroyView() {
        soundsAdapter.dispose()
        viewModel.onDestroyed()
        compositeDisposable.clear()
        super.onDestroyView()
    }

}
