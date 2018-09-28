package net.yuzumone.tootrus.data.mastodon

import com.sys1yagi.mastodon4j.MastodonClient
import com.sys1yagi.mastodon4j.api.entity.Status
import com.sys1yagi.mastodon4j.api.method.Statuses
import javax.inject.Inject
import javax.inject.Named

interface StatusRepository {
    fun postStatus(status: String,
                   inReplyToId: Long?,
                   mediaIds: List<Long>?,
                   sensitive: Boolean,
                   spoilerText: String?,
                   visibility: Status.Visibility
    ): Status
    fun postFavorite(id: Long): Status
}

class DefaultStatusRepository @Inject constructor(
        @Named("client") private val client: MastodonClient
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

    override fun postFavorite(id: Long): Status {
        return Statuses(client).postFavourite(id).execute()
    }
}