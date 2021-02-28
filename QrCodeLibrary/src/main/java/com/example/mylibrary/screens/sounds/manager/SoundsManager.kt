package com.example.mylibrary.screens.sounds.manager

import android.util.Log
import android.webkit.URLUtil
import com.example.mylibrary.network.api.SoundsAPI
import com.example.mylibrary.network.base.IBaseNetworkManager
import com.example.mylibrary.network.data.SoundData
import com.example.mylibrary.repo.ISoundsRepository
import com.example.mylibrary.screens.sounds.list.SoundItemEvent
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

class SoundsManager(
    private val network: IBaseNetworkManager,
    private val soundsRepository: ISoundsRepository
) : ISoundsManager {

    private val compositeDisposable = CompositeDisposable()
    private val fetchSounds = PublishSubject.create<List<SoundData?>?>()

    private val soundsAPI = network.buildRetrofit()
        ?.create(SoundsAPI::class.java)

    init {

        compositeDisposable.add(
            fetchSounds
                .map { soundsList ->
                    soundsList
                        .map { it?.soundLink.toString() }
                        .toList()
                }
                .flatMap { linksSoundsList ->

                    Observable.fromIterable(linksSoundsList)
                        .flatMap { link ->
                            soundsAPI
                                ?.downloadAudio(link)
                                ?.subscribeOn(Schedulers.io())
                                ?.toFlowable(BackpressureStrategy.MISSING)
                                ?.doOnNext { response ->
                                    soundsRepository.addSound(response, link)
                                }
                                ?.toObservable()
                        }
                }
                .subscribe({}, (Throwable::printStackTrace))
        )


    }

    override fun fetchData(
        url: String,
        currentSoundItem: BehaviorSubject<SoundItemEvent>
    ): Single<List<SoundItemEvent?>>? {

        return soundsAPI
            ?.getSoundsList(url)
            ?.subscribeOn(Schedulers.io())
            ?.map { dataList ->
                dataList
                    .filter { soundData -> isValidUrl(soundData.soundLink.toString()) }
                    .groupBy { soundData -> soundData.soundLink }
                    .map { dataMap -> dataMap.value.firstOrNull() }
                    .toList()
            }
            ?.doOnEvent { dataList, t2 ->
                fetchSounds.onNext(dataList)
            }
            ?.map { soundsList ->
                generateDataList(soundsList, currentSoundItem)
            }
    }

    private fun generateDataList(
        data: List<SoundData?>,
        currentSoundItem: BehaviorSubject<SoundItemEvent>
    ): List<SoundItemEvent> {
        return data.map { soundData ->
            SoundItemEvent(
                soundData?.soundID,
                soundData?.soundName,
                soundData?.soundLink,
                currentSoundItem
            )
        }
            .toList()
    }


    private fun isValidUrl(url: String?): Boolean =
        url?.endsWith(".wav") ?: false && URLUtil.isValidUrl(url)

    override fun onDestroyed() {
        compositeDisposable.clear()
    }

    companion object {
        enum class SoundState {
            LOADING,
            PLAYING,
            END,
            NONE,

        }
    }

}