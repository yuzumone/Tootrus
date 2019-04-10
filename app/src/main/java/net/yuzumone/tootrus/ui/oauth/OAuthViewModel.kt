package net.yuzumone.tootrus.ui.oauth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.yuzumone.tootrus.domain.Failure
import net.yuzumone.tootrus.domain.Success
import net.yuzumone.tootrus.domain.mastodon.oauth.GetAccessTokenUseCase
import net.yuzumone.tootrus.domain.mastodon.oauth.GetOAuthParameterUseCase
import net.yuzumone.tootrus.domain.mastodon.oauth.Params
import net.yuzumone.tootrus.domain.prefs.StoreInstanceNameAndTokenUseCase
import net.yuzumone.tootrus.vo.OAuthParameter
import javax.inject.Inject

class OAuthViewModel @Inject constructor(
        private val getOAuthParameterUseCase: GetOAuthParameterUseCase,
        private val getAccessTokenUseCase: GetAccessTokenUseCase,
        private val storeInstanceNameAndTokenUseCase: StoreInstanceNameAndTokenUseCase
) : ViewModel() {

    val oauthParameter = MutableLiveData<OAuthParameter>()
    val oauthParameterError = MutableLiveData<Exception>()
    val transactionMainView = MutableLiveData<Boolean>()
    val accessTokenError = MutableLiveData<Exception>()

    fun getOAuthParameter(instanceName: String?) {
        val name = if (instanceName.isNullOrBlank()) "social.mikutter.hachune.net" else instanceName
        getOAuthParameterUseCase(name) {
            when (it) {
                is Success -> oauthParameter.value = it.value
                is Failure -> oauthParameterError.value = it.reason
            }
        }
    }

    fun getAccessToken(code: String?) {
        val params = Params(oauthParameter.value!!.instanceName, oauthParameter.value!!.clientId,
                oauthParameter.value!!.clientSecret, code)
        getAccessTokenUseCase(params) { token ->
            when (token) {
                is Success -> {
                    val data = Pair(oauthParameter.value!!.instanceName, token.value.accessToken)
                    storeInstanceNameAndTokenUseCase(data) {
                        when (it) {
                            is Success -> {
                                transactionMainView.value = true
                            }
                            is Failure -> {
                                accessTokenError.value = it.reason
                            }
                        }
                    }
                }
                is Failure -> accessTokenError.value = token.reason
            }
        }
    }
}