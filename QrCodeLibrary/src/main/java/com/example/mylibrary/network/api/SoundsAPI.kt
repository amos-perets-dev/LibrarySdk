package com.example.mylibrary.network.api

import com.example.mylibrary.network.data.SoundData
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url


interface SoundsAPI {

    /**
     * Call to get the sounds from the server
     */
    @GET
    fun getSoundsList(@Url url: String): Single<List<SoundData>>?

    /**
     * Call to download the audio file
     */
    @Streaming
    @GET
    fun downloadAudio(@Url url: String): Observable<Response<ResponseBody?>?>?


}