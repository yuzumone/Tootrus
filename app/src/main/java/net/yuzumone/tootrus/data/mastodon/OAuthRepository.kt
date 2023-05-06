package net.yuzumone.tootrus.data.mastodon

import com.google.gson.Gson
import com.sys1yagi.mastodon4j.MastodonClient
import com.sys1yagi.mastodon4j.api.Scope
import com.sys1yagi.mastodon4j.api.entity.auth.AccessToken
import com.sys1yagi.mastodon4j.api.method.Apps
import net.yuzumone.tootrus.vo.OAuthParameter
import okhttp3.OkHttpClient
import javax.inject.Inject

interface OAuthRepository {
    fun getOAuthParameter(instanceName: String): OAuthParameter
    fun getAccessToken(instanceName: String, clientId: String, clientSecret: String, code: String):
            AccessToken
}

class DefaultOAuthRepository @Inject constructor(
    private val okHttpClientBuilder: OkHttpClient.Builder,
    private val gson: Gson
) : OAuthRepository {

    override fun getOAuthParameter(instanceName: String): OAuthParameter {
        val client = MastodonClient.Builder(instanceName, okHttpClientBuilder, gson).build()
        val apps = Apps(client)
        val registration = apps.createApp(
            clientName = "Tootrus",
            redirectUris = "urn:ietf:wg:oauth:2.0:oob",
            scope = Scope(Scope.Name.ALL),
            website = "https://github.com/yuzumone/Tootrus"
        ).execute()
        val url = apps.getOAuthUrl(registration.clientId, Scope(Scope.Name.ALL))
        return OAuthParameter(instanceName, registration.clientId, registration.clientSecret, url)
    }

    override fun getAccessToken(
        instanceName: String, clientId: String, clientSecret: String,
        code: String
    ): AccessToken {
        val client = MastodonClient.Builder(instanceName, okHttpClientBuilder, gson).build()
        val apps = Apps(client)
        return apps.getAccessToken(
            clientId = clientId,
            clientSecret = clientSecret,
            code = code,
            grantType = "authorization_code"
        ).execute()
    }
}
