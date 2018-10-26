package net.yuzumone.tootrus.domain.mastodon.account

import com.sys1yagi.mastodon4j.api.entity.Relationship
import net.yuzumone.tootrus.data.mastodon.AccountRepository
import net.yuzumone.tootrus.domain.UseCase
import javax.inject.Inject

class GetRelationShipsUseCase @Inject constructor(
        private val repository: AccountRepository
) : UseCase<List<Long>, List<Relationship>>() {
    override suspend fun run(params: List<Long>) = repository.getRelationShips(params)
}