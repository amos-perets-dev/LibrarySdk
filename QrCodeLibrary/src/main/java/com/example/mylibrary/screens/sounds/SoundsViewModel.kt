package com.example.mylibrary.screens.sounds

import android.util.Log
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.example.mylibrary.R
import com.example.mylibrary.library.init.QrCodeScanConfiguration
import com.example.mylibrary.utils.SingleLiveEvent
import com.example.mylibrary.network.error.IHandleNetworkError
import com.example.mylibrary.screens.sounds.manager.ISoundsManager
import com.example.mylibrary.screens.sounds.list.SoundItemEvent
import com.example.mylibrary.screens.sounds.manager.SoundsManager
import com.example.mylibrary.utils.media.IMediaPlayerHelper
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class SoundsViewModel(
    private val soundsManager: ISoundsManager,
    private val handleNetworkError: IHandleNetworkError,
    private val mediaPlayerHelper: IMediaPlayerHelper,
    private val qrCodeScanConfiguration: QrCodeScanConfiguration
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val dataResult = SingleLiveEvent<DataResult>()
    private val currentSoundItem = BehaviorSubject.create<SoundItemEvent>()

    init {

        currentSoundItem
            .hide()
            .subscribeOn(Schedulers.io())
            ?.filter { soundDataEvent ->
                soundDataEvent.state == SoundsManager.Companion.SoundState.LOADING.ordinal
            }
            ?.doOnNext { soundDataEvent ->
                mediaPlayerHelper.play(soundDataEvent.soundLink.toString())
            }
            ?.switchMap (this::playSoundResultAsync)
            ?.subscribe {

            }?.let {
                compositeDisposable.add(
                    it
                )
            }

    }

    /**
     * Get the progress file state
     */
    private fun playSoundResultAsync(soundDataEvent: SoundItemEvent): Observable<Boolean>? {
        return mediaPlayerHelper.isStartPlay()
            .doOnNext { isStartPlay ->
                notifyNewState(soundDataEvent, isStartPlay)
            }
    }

    /**
     * Call when needed to set the state of the item sound
     */
    private fun notifyNewState(soundDataEvent: SoundItemEvent, isStartPlay: Boolean) {
        val state = if (isStartPlay) {
            SoundsManager.Companion.SoundState.PLAYING.ordinal
        } else {
            SoundsManager.Companion.SoundState.END.ordinal
        }
        soundDataEvent.state = state

        currentSoundItem.onNext(soundDataEvent)
    }

    /**
     * Call when the scanner pare the qrcode
     */
    fun qrCodeDataChange(data: String?, error: Throwable?) {

        if (error == null) {
            dataResult.postValue(DataResult.Loading)
        }

        soundsManager.fetchData(data.toString(), currentSoundItem)
            ?.subscribe({ result ->

                dataResult.postValue(
                    if (result.isEmpty()) {
                        DataResult.Msg(
                            R.string.sounds_screen_empty_msg_text,
                            qrCodeScanConfiguration.emptyMsgColor
                        )
                    } else {
                        DataResult.Success(result)
                    }
                )

            }, { exception ->

                dataResult.postValue(
                    DataResult.Msg(
                        handleNetworkError.generateErrorID(exception),
                        qrCodeScanConfiguration.errorMsgColor
                    )
                )

            })?.let {
                compositeDisposable.add(
                    it
                )
            }
    }

    sealed class DataResult {
        data class Success(val data: List<SoundItemEvent?>?) : DataResult()
        data class Msg(@StringRes val message: Int, @ColorRes val messageColor: Int) : DataResult()
        object Loading : DataResult()
    }

    override fun onCleared() {
        soundsManager.onDestroyed()
        mediaPlayerHelper.onDestroyed()
        compositeDisposable.clear()
        super.onCleared()
    }

    /**
     * Call when the page closed
     */
    fun onDestroyed() {
        soundsManager.onDestroyed()
        mediaPlayerHelper.onDestroyed()
        compositeDisposable.clear()
    }

}