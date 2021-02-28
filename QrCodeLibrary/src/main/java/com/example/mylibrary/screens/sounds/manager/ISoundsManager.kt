package com.example.mylibrary.screens.sounds.manager

import com.example.mylibrary.screens.sounds.list.SoundItemEvent
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject

interface ISoundsManager {

    /**
     * Call to fetch the data from the server
     */
    fun fetchData(
        url: String,
        currentSoundItem: BehaviorSubject<SoundItemEvent>
    ): Single<List<SoundItemEvent?>>?

    /**
     * Call when the page closed
     */
    fun onDestroyed()
}