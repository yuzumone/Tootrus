package net.yuzumone.tootrus.ui.oauth

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.sys1yagi.mastodon4j.api.entity.auth.AccessToken
import com.sys1yagi.mastodon4j.api.exception.Mastodon4jRequestException
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import net.yuzumone.tootrus.data.mastodon.OAuthRepository
import net.yuzumone.tootrus.data.prefs.PreferenceStorage
import net.yuzumone.tootrus.vo.OAuthParameter
import javax.inject.Inject

class OAuthViewModel @Inject constructor(
        private val repository: OAuthRepository,
        private val preferenceStorage: PreferenceStorage
) : ViewModel() {

    val oauthParameter = MutableLiveData<OAuthParameter>()
    val oauthParameterError = MutableLiveData<Mastodon4jRequestException>()
    val accessToken = MutableLiveData<AccessToken>()
    val accessTokenError = MutableLiveData<Exception>()

    fun getOAuthParameter(instanceName: String?) = launch {
        val name = if (instanceName.isNullOrBlank()) "social.mikutter.hachune.net" else instanceName!!
        try {
            val param = async { repository.getOAuthParameter(name) }.await()
            oauthParameter.postValue(param)
        } catch (e: Mastodon4jRequestException) {
            oauthParameterError.postValue(e)
        }
    }

    fun getAccessToken(code: String?) = launch {
        try {
            val token = async { repository.getAccessToken(
                    instanceName = oauthParameter.value!!.instanceName,
                    clientId = oauthParameter.value!!.clientId,
                    clientSecret = oauthParameter.value!!.clientSecret,
                    code = code!!
            ) }.await()
            preferenceStorage.accessToken = token.accessToken
            accessToken.postValue(token)
        } catch (e: Mastodon4jRequestException) {
            accessTokenError.postValue(e)
        } catch (e: NullPointerException) {
            accessTokenError.postValue(e)
        }
    }
}