package net.yuzumone.tootrus.data.mastodon

import com.sys1yagi.mastodon4j.MastodonClient
import com.sys1yagi.mastodon4j.api.entity.Status
import com.sys1yagi.mastodon4j.api.method.Statuses
import net.yuzumone.tootrus.vo.TootrusStatus
import javax.inject.Inject
import javax.inject.Named

interface StatusRepository {
    fun postStatus(status: String,
                   inReplyToId: Long?,
                   mediaIds: List<Long>?,
                   sensitive: Boolean,
                   spoilerText: String?,
                   visibility: Status.Visibility
    ): TootrusStatus
    fun postFavorite(id: Long): TootrusStatus
}

class DefaultStatusRepository @Inject constructor(
        @Named("client") private val client: MastodonClient
) : StatusRepository {
    override fun postStatus(status: String,
                            inReplyToId: Long?,
                            mediaIds: List<Long>?,
                            sensitive: Boolean,
                            spoilerText: String?,
                            visibility: Status.Visibility): TootrusStatus {
        val s = Statuses(client).postStatus(
                status = status,
                inReplyToId = inReplyToId,
                mediaIds = mediaIds,
                sensitive = sensitive,
                spoilerText = spoilerText,
                visibility = visibility
        ).execute()
        return TootrusStatus(s)
    }

    override fun postFavorite(id: Long): TootrusStatus {
        return TootrusStatus(Statuses(client).postFavourite(id).execute())
    }
}