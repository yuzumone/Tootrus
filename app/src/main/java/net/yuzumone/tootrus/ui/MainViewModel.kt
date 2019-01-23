package net.yuzumone.tootrus.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.sys1yagi.mastodon4j.api.entity.Account
import dagger.Lazy
import net.yuzumone.tootrus.domain.Success
import net.yuzumone.tootrus.domain.mastodon.account.GetVerifyCredentialsUseCase
import net.yuzumone.tootrus.domain.prefs.GetAccessTokenPrefUseCase
import net.yuzumone.tootrus.domain.prefs.StoreUserIdPrefUseCase
import net.yuzumone.tootrus.util.map
import javax.inject.Inject

class MainViewModel @Inject constructor(
        private val storeUserIdPrefUseCase: StoreUserIdPrefUseCase,
        getAccessTokenUseCase: GetAccessTokenPrefUseCase
) : ViewModel() {

    private val accessToken = MutableLiveData<String>()
    val setFragment: LiveData<SetFragment>
    val account =  MutableLiveData<Account>()
    @Inject lateinit var getLazyVerifyCredentialUseCase: Lazy<GetVerifyCredentialsUseCase>

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

    fun getCredentials() {
        val getVerifyCredentialsUseCase = getLazyVerifyCredentialUseCase.get()
        getVerifyCredentialsUseCase(Unit) {
            when (it) {
                is Success -> {
                    storeUserIdPrefUseCase(it.value.id)
                    account.value = it.value
                }
            }
        }
    }
}

enum class SetFragment {
    OAUTH,
    TOP
}