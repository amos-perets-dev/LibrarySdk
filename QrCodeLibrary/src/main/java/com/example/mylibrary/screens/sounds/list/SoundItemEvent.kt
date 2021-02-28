package com.example.mylibrary.screens.sounds.list

import com.example.mylibrary.screens.sounds.manager.SoundsManager
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class SoundItemEvent(
    private val soundID: Int? = null,
    val soundName: String? = null,
    val soundLink: String? = null,
    private val soundClick: BehaviorSubject<SoundItemEvent>,
    var state: Int? = null
) {

    /**
     * Get the item state async [SoundsManager.Companion.SoundState]
     */
    fun getItemStateAsync(): Observable<Int>? {
        return getClick()
            ?.map {

                if (it.soundID == soundID) {
                    state
                } else {
                    SoundsManager.Companion.SoundState.NONE.ordinal
                }

            }
    }

    /**
     * Call when the user click on the audio file[SoundItemEvent]
     */
    fun onClick() {
        state = SoundsManager.Companion.SoundState.LOADING.ordinal
        soundClick.onNext(this)
    }

    private fun getClick(): Observable<SoundItemEvent>? {
        return soundClick
            .hide()
            .subscribeOn(Schedulers.io())
//            .distinctUntilChanged()
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other is SoundItemEvent) {
            return other.soundID == soundID
                    && other.state == state
        }

        return false
    }

    override fun toString(): String {
        return "SoundItemEvent(soundID=$soundID, soundName=$soundName, soundLink=$soundLink, soundClick=$soundClick, state=$state)"
    }


}