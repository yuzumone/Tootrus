package net.yuzumone.tootrus.domain.mastodon.account

import com.sys1yagi.mastodon4j.api.Range
import com.sys1yagi.mastodon4j.api.entity.Account
import net.yuzumone.tootrus.data.mastodon.AccountRepository
import net.yuzumone.tootrus.domain.UseCase
import javax.inject.Inject

class GetFollowersUseCase @Inject constructor(
    private val repository: AccountRepository
) : UseCase<Pair<Long, Range>, List<Account>>() {
    override suspend fun run(params: Pair<Long, Range>) =
        repository.getFollowers(params.first, params.second)
}
