package net.yuzumone.tootrus.util

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations

fun <X, Y> LiveData<X>.map(body: (X) -> Y): LiveData<Y> {
    return Transformations.map(this, body)
}

fun <T> MutableLiveData<List<T>>.insertValue(value: T) {
    val list = arrayListOf<T>()
    this.value?.forEach { list.add(it) }
    list.add(0, value)
    this.value = list
}

fun <T> MutableLiveData<List<T>>.postInsertValue(value: T) {
    val list = arrayListOf<T>()
    this.value?.forEach { list.add(it) }
    list.add(0, value)
    this.postValue(list)
}