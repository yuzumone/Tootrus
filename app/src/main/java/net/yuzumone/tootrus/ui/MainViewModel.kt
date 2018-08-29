package net.yuzumone.tootrus.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import net.yuzumone.tootrus.domain.Success
import net.yuzumone.tootrus.domain.prefs.GetAccessTokenPrefUseCase
import net.yuzumone.tootrus.util.map
import javax.inject.Inject

class MainViewModel @Inject constructor(
        getAccessTokenUseCase: GetAccessTokenPrefUseCase
) : ViewModel() {

    private val accessToken = MutableLiveData<String>()
    val setFragment: LiveData<SetFragment>

    init {
        getAccessTokenUseCase(Unit) {
            when (it) {
                is Success -> accessToken.value = it.value
            }
        }
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