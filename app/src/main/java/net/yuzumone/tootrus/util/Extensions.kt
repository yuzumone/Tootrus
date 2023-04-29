package net.yuzumone.tootrus.util

import androidx.lifecycle.MutableLiveData
import com.sys1yagi.mastodon4j.api.entity.Status

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

fun <T> MutableLiveData<List<T>>.replaceValue(old: T, new: T) {
    val list = arrayListOf<T>()
    this.value?.forEach {
        if (it == old) {
            list.add(new)
        } else {
            list.add(it)
        }
    }
    this.value = list
}

fun MutableLiveData<List<Status>>.replaceStatus(old: Status, new: Status) {
    val list = arrayListOf<Status>()
    this.value?.forEach {
        if (it.id == old.id) {
            list.add(new)
        } else {
            list.add(it)
        }
    }
    this.value = list
}
