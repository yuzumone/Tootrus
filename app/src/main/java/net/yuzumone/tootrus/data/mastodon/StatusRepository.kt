package net.yuzumone.tootrus.data.mastodon

import com.sys1yagi.mastodon4j.MastodonClient
import com.sys1yagi.mastodon4j.api.entity.Status
import com.sys1yagi.mastodon4j.api.method.Statuses
import javax.inject.Inject

interface StatusRepository {
    fun postStatus(status: String,
                   inReplyToId: Long?,
                   mediaIds: List<Long>?,
                   sensitive: Boolean,
                   spoilerText: String?,
                   visibility: Status.Visibility
    ): Status
}

class DefaultStatusRepository @Inject constructor(
        private val client: MastodonClient
) : StatusRepository {
    override fun postStatus(status: String,
                            inReplyToId: Long?,
                            mediaIds: List<Long>?,
                            sensitive: Boolean,
                            spoilerText: String?,
                            visibility: Status.Visibility): Status {
        return Statuses(client).postStatus(
                status = status,
                inReplyToId = inReplyToId,
                mediaIds = mediaIds,
                sensitive = sensitive,
                spoilerText = spoilerText,
                visibility = visibility
        ).execute()
    }
}