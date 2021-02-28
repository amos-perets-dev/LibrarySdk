package com.example.mylibrary.utils.media

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import com.example.mylibrary.repo.ISoundsRepository
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.io.IOException


class MediaPlayerHelper(
    private val context: Context,
    private val soundsRepository: ISoundsRepository
) :
    IMediaPlayerHelper {
    private var mMediaPlayer: MediaPlayer? = null
    private val isStartPlay = PublishSubject.create<Boolean>()

    override fun play(dataSource: String) {

        try {
            stopSound()

            val sounds = soundsRepository.getSound(dataSource)


            mMediaPlayer = MediaPlayer().apply {
                setDataSource(context, sounds ?: Uri.parse(dataSource))
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )
                prepareAsync()
            }
            mMediaPlayer?.setOnCompletionListener {
                isStartPlay.onNext(false)
            }

            mMediaPlayer?.setOnPreparedListener {
                isStartPlay.onNext(true)
                it.start()
            }

            mMediaPlayer?.setOnBufferingUpdateListener { mediaPlayer, i ->

            }
        } catch (e: IOException) {
            mMediaPlayer = null
            mMediaPlayer?.release()
        }
    }

    override fun isStartPlay(): Observable<Boolean> = isStartPlay.hide().distinctUntilChanged()

    private fun stopSound() {
        if (mMediaPlayer != null) {
            mMediaPlayer?.stop()
            mMediaPlayer?.release()
        }
    }

    /**
     *  Closes the MediaPlayer when the app is closed
     */
    override fun onDestroyed() {
        stopSound()
        mMediaPlayer = null

    }

}