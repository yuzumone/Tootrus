package net.yuzumone.tootrus.domain.mastodon.account

import com.sys1yagi.mastodon4j.api.Range
import com.sys1yagi.mastodon4j.api.entity.Status
import net.yuzumone.tootrus.data.mastodon.AccountRepository
import net.yuzumone.tootrus.domain.UseCase
import javax.inject.Inject

class GetStatusesUseCase @Inject constructor(
    private val repository: AccountRepository
) : UseCase<Params, List<Status>>() {
    override suspend fun run(params: Params) =
        repository.getStatuses(
            params.id,
            params.onlyMedia,
            params.excludeReplies,
            params.pinned,
            params.range
        )
}

data class Params(
    val id: Long,
    val onlyMedia: Boolean,
    val excludeReplies: Boolean,
    val pinned: Boolean,
    val range: Range
)
