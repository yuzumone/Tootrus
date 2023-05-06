package net.yuzumone.tootrus.domain.mastodon.account

import com.sys1yagi.mastodon4j.api.entity.Account
import net.yuzumone.tootrus.data.mastodon.AccountRepository
import net.yuzumone.tootrus.domain.UseCase
import javax.inject.Inject

class GetVerifyCredentialsUseCase @Inject constructor(
    private val repository: AccountRepository
) : UseCase<Unit, Account>() {
    override suspend fun run(params: Unit) = repository.getVerifyCredentials()
}
