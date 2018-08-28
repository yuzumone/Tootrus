package net.yuzumone.tootrus.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import net.yuzumone.tootrus.data.prefs.PreferenceStorage
import net.yuzumone.tootrus.util.map
import javax.inject.Inject

class MainViewModel @Inject constructor(
        preferenceStorage: PreferenceStorage
) : ViewModel() {

    private val accessToken = MutableLiveData<String>()
    val setFragment: LiveData<SetFragment>

    init {
        accessToken.value = preferenceStorage.accessToken
        setFragment = accessToken.map {
            if (it == null) {
                SetFragment.OAUTH
            } else {
                SetFragment.TOP
            }
        }
    }
}

enum class SetFragment {
    OAUTH,
    TOP
}