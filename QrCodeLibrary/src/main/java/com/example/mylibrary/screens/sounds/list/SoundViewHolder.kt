package com.example.mylibrary.screens.sounds.list

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mylibrary.R
import com.example.mylibrary.screens.sounds.manager.SoundsManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.sound_item.view.*

class SoundViewHolder(
    parent: ViewGroup,
    private val compositeDisposable: CompositeDisposable
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(
        R.layout.sound_item,
        parent,
        false
    )
) {

    /**
     * Call to binding the data
     */
    fun bindData(model: SoundItemEvent?) {
        itemView.sound_name.text = model?.soundName
        itemView.setOnClickListener { model?.onClick() }

        val progressLoadingSound = itemView.progress_loading_sound
        val iconSoundState = itemView.icon_sound_state
        compositeDisposable.addAll(
            model
                ?.getItemStateAsync()
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe({ state ->

                    when (state) {
                        SoundsManager.Companion.SoundState.LOADING.ordinal -> {
                            progressLoadingSound.visibility = View.VISIBLE
                            iconSoundState.visibility = View.INVISIBLE
                        }
                        SoundsManager.Companion.SoundState.PLAYING.ordinal -> {
                            progressLoadingSound.visibility = View.INVISIBLE
                            changeIcon(R.drawable.ic_sound)
                        }
                        else -> {
                            changeIcon(R.drawable.ic_play_button)
                        }
                    }

                }, (Throwable::printStackTrace))

        )

    }

    /**
     * Call to change the icon of the sound state
     */
    private fun changeIcon(@DrawableRes drawableRes: Int) {
        val iconSoundState = itemView.icon_sound_state
        iconSoundState.setImageDrawable(
            ContextCompat.getDrawable(
                itemView.context,
                drawableRes
            )
        )
        iconSoundState.visibility = View.VISIBLE
    }

}