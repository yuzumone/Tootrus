package net.yuzumone.tootrus.ui.oauth

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.sys1yagi.mastodon4j.api.entity.auth.AccessToken
import net.yuzumone.tootrus.domain.Failure
import net.yuzumone.tootrus.domain.Success
import net.yuzumone.tootrus.domain.mastodon.oauth.GetAccessTokenUseCase
import net.yuzumone.tootrus.domain.mastodon.oauth.GetOAuthParameterUseCase
import net.yuzumone.tootrus.domain.mastodon.oauth.Params
import net.yuzumone.tootrus.domain.prefs.StoreAccessTokenPrefUseCase
import net.yuzumone.tootrus.vo.OAuthParameter
import javax.inject.Inject

class OAuthViewModel @Inject constructor(
        private val getOAuthParameterUseCase: GetOAuthParameterUseCase,
        private val getAccessTokenUseCase: GetAccessTokenUseCase,
        private val storeAccessTokenPrefUseCase: StoreAccessTokenPrefUseCase
) : ViewModel() {

    val oauthParameter = MutableLiveData<OAuthParameter>()
    val oauthParameterError = MutableLiveData<Exception>()
    val accessToken = MutableLiveData<AccessToken>()
    val accessTokenError = MutableLiveData<Exception>()

    fun getOAuthParameter(instanceName: String?) {
        val name = if (instanceName.isNullOrBlank()) "social.mikutter.hachune.net" else instanceName!!
        getOAuthParameterUseCase(name) {
            when (it) {
                is Success -> oauthParameter.value = it.value
                is Failure -> oauthParameterError.value = it.reason
            }
        }
    }

    fun getAccessToken(code: String?) {
        getAccessTokenUseCase(Params(
                oauthParameter.value!!.instanceName, oauthParameter.value!!.clientId,
                oauthParameter.value!!.clientSecret, code)) {
                    when (it) {
                        is Success -> {
                            accessToken.value = it.value
                            storeAccessTokenPrefUseCase(it.value.accessToken)
                        }
                        is Failure -> accessTokenError.value = it.reason
                    }
                }
    }
}