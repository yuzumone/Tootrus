package net.yuzumone.tootrus.domain.mastodon.account

import com.sys1yagi.mastodon4j.api.entity.Account
import com.sys1yagi.mastodon4j.api.entity.Relationship
import net.yuzumone.tootrus.data.mastodon.AccountRepository
import net.yuzumone.tootrus.domain.UseCase
import javax.inject.Inject

class GetAccountAndRelationShipUseCase @Inject constructor(
        private val repository: AccountRepository
) : UseCase<Long, Pair<Account, Relationship>>() {
    override suspend fun run(params: Long): Pair<Account, Relationship> {
        val account = repository.getAccount(params)
        val relationship = repository.getRelationShips(arrayListOf(params)).first()
        return Pair(account, relationship)
    }
}