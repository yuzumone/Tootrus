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

fun <T> MutableLiveData<List<T>>.insertValues(values: List<T>) {
    val list = arrayListOf<T>()
    this.value?.forEach { list.add(it) }
    values.reversed().forEach { list.add(0, it) }
    this.value = list
}

fun <T> MutableLiveData<List<T>>.postInsertValues(values: List<T>) {
    val list = arrayListOf<T>()
    this.value?.forEach { list.add(it) }
    values.reversed().forEach { list.add(0, it) }
    this.postValue(list)
}