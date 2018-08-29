package net.yuzumone.tootrus.domain.mastodon.oauth

import com.sys1yagi.mastodon4j.api.entity.auth.AccessToken
import net.yuzumone.tootrus.data.mastodon.OAuthRepository
import net.yuzumone.tootrus.domain.UseCase
import javax.inject.Inject

class GetAccessTokenUseCase @Inject constructor(
        private val repository: OAuthRepository
) : UseCase<Params, AccessToken>() {
    override suspend fun run(params: Params) = repository.getAccessToken(
            params.instanceName!!, params.clientId!!, params.clientSecret!!, params.code!!)
}

data class Params(
        val instanceName: String?,
        val clientId: String?,
        val clientSecret: String?,
        val code: String?
)