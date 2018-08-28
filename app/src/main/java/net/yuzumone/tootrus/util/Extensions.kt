package net.yuzumone.tootrus.util

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations

fun <X, Y> LiveData<X>.map(body: (X) -> Y): LiveData<Y> {
    return Transformations.map(this, body)
}