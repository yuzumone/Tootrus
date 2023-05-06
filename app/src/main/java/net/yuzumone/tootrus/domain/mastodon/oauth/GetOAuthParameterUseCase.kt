package net.yuzumone.tootrus.domain.mastodon.oauth

import net.yuzumone.tootrus.data.mastodon.OAuthRepository
import net.yuzumone.tootrus.domain.UseCase
import net.yuzumone.tootrus.vo.OAuthParameter
import javax.inject.Inject

class GetOAuthParameterUseCase @Inject constructor(
    private val repository: OAuthRepository
) : UseCase<String, OAuthParameter>() {
    override suspend fun run(params: String) = repository.getOAuthParameter(params)
}
