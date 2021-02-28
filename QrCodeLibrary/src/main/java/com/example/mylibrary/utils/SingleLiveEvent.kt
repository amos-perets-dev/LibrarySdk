package com.example.mylibrary.utils

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer


class SingleLiveEvent <T> : MutableLiveData<T>() {

    private val pendingObservers = HashMap<Observer<*>, Boolean>()

    override fun observe(
        owner: LifecycleOwner,
        observer: Observer<in T>
    ) {
        pendingObservers[observer] = false
        super.observe(owner, Observer { value ->
            if (pendingObservers.put(observer, false) == true) {
                observer.onChanged(value)
            }
        })
    }

    override fun removeObservers(owner: LifecycleOwner) {
        pendingObservers.clear()
        super.removeObservers(owner)
    }

    override fun removeObserver(observer: Observer<in T>) {
        pendingObservers.remove(observer)
        super.removeObserver(observer)
    }

    override fun setValue(value: T) {
        pendingObservers.entries.forEach { it.setValue(true) }
        super.setValue(value)
    }

    override fun postValue(value: T) {
        pendingObservers.entries.forEach { it.setValue(true) }
        super.postValue(value)
    }
}
