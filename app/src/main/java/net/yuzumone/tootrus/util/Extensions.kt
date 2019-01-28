package net.yuzumone.tootrus.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.viewpager.widget.ViewPager
import com.sys1yagi.mastodon4j.api.entity.Status

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

fun ViewPager.addOnPageChangeListener(
        onPageScrollStateChanged: (Int) -> Unit,
        onPageScrolled: (Int, Float, Int) -> Unit,
        onPageSelected: (Int) -> Unit
) = addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
    override fun onPageScrollStateChanged(state: Int): Unit = onPageScrollStateChanged(state)
    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int): Unit =
            onPageScrolled(position, positionOffset, positionOffsetPixels)
    override fun onPageSelected(position: Int): Unit = onPageSelected(position)
})