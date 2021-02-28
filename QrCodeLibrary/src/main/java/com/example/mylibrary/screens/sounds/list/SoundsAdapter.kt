package com.example.mylibrary.screens.sounds.list

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.disposables.CompositeDisposable

class SoundsAdapter(private var soundsList: List<SoundItemEvent?>? = arrayListOf()) :
    RecyclerView.Adapter<SoundViewHolder>() {

    private val compositeDisposable = CompositeDisposable()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoundViewHolder {
        return SoundViewHolder(
            parent, compositeDisposable
        )
    }

    override fun getItemCount(): Int = soundsList?.size ?: 0

    override fun onBindViewHolder(holder: SoundViewHolder, position: Int) {
        holder.bindData(soundsList?.get(position))
    }

    /**
     * Call when the data is ready
     */
    fun setData(soundsList: List<SoundItemEvent?>?){
        this.soundsList = soundsList
        notifyDataSetChanged()
    }

    /**
     * Call when the page closed
     */
    fun dispose(){
        compositeDisposable.clear()
    }
}