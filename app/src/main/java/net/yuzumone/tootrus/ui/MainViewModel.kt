package net.yuzumone.tootrus.ui

import androidx.lifecycle.*
import com.sys1yagi.mastodon4j.api.entity.Account
import dagger.Lazy
import net.yuzumone.tootrus.domain.Failure
import net.yuzumone.tootrus.domain.Success
import net.yuzumone.tootrus.domain.mastodon.account.GetVerifyCredentialsUseCase
import net.yuzumone.tootrus.domain.prefs.GetAccessTokenPrefUseCase
import net.yuzumone.tootrus.domain.prefs.StoreUserIdPrefUseCase
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val storeUserIdPrefUseCase: StoreUserIdPrefUseCase,
    getAccessTokenUseCase: GetAccessTokenPrefUseCase
) : ViewModel() {

    private val accessToken = MutableLiveData<String>()
    val setFragment: LiveData<SetFragment>
    val account = MutableLiveData<Account>()
    val eventTransactionToTop = MutableLiveData<Unit>()
    @Inject
    lateinit var getLazyVerifyCredentialUseCase: Lazy<GetVerifyCredentialsUseCase>

    init {
        getAccessTokenUseCase(Unit, viewModelScope) {
            when (it) {
                is Success -> accessToken.value = it.value
                is Failure -> TODO()
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
        getVerifyCredentialsUseCase(Unit, viewModelScope) {
            when (it) {
                is Success -> {
                    storeUserIdPrefUseCase(it.value.id, viewModelScope)
                    account.value = it.value
                }
                is Failure -> TODO()
            }
        }
    }
}

enum class SetFragment {
    OAUTH,
    TOP
}
