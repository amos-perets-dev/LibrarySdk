package com.example.mylibrary.network.data

import com.google.gson.annotations.SerializedName

data class SoundData(
    @SerializedName("id")
    val soundID: Int? = null,

    @SerializedName("name")
    val soundName: String? = null,

    @SerializedName("url")
    val soundLink: String? = null


    ) {
}