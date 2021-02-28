package com.example.mylibrary.utils.media

import io.reactivex.Observable

interface IMediaPlayerHelper {

    /**
     * Call when the user click on start the music
     */
    fun play(dataSource : String)

    /**
     * Get the [Observable] of the music state start or end
     */
    fun isStartPlay() : Observable<Boolean>

    /**
     * Call when the page closed
     */
    fun onDestroyed()
}