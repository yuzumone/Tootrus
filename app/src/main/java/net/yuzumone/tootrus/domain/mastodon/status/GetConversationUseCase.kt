package net.yuzumone.tootrus.domain.mastodon.status

import com.sys1yagi.mastodon4j.api.entity.Status
import net.yuzumone.tootrus.data.mastodon.StatusRepository
import net.yuzumone.tootrus.domain.UseCase
import javax.inject.Inject

class GetConversationUseCase @Inject constructor(
    private val repository: StatusRepository
) : UseCase<Status, List<Status>>() {
    override suspend fun run(params: Status): List<Status> {
        val statuses = ArrayList<Status>()
        statuses.add(params)
        var origin = params
        while (origin.inReplyToId != null) {
            val status = repository.getStatus(origin.inReplyToId!!)
            statuses.add(status)
            origin = status
        }
        return statuses
    }
}
