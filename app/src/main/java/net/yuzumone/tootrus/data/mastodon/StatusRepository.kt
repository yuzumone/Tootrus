package net.yuzumone.tootrus.data.mastodon

import com.sys1yagi.mastodon4j.MastodonClient
import com.sys1yagi.mastodon4j.api.entity.Status
import com.sys1yagi.mastodon4j.api.method.Statuses
import net.yuzumone.tootrus.di.InterceptorMastodonClient
import javax.inject.Inject

interface StatusRepository {
    fun postStatus(
        status: String,
        inReplyToId: Long?,
        mediaIds: List<Long>?,
        sensitive: Boolean,
        spoilerText: String?,
        visibility: Status.Visibility
    ): Status

    fun getStatus(id: Long): Status
    fun postFavorite(id: Long): Status
    fun postUnfavorite(id: Long): Status
    fun postReblog(id: Long): Status
}

class DefaultStatusRepository @Inject constructor(
    @InterceptorMastodonClient private val client: MastodonClient
) : StatusRepository {
    override fun postStatus(
        status: String,
        inReplyToId: Long?,
        mediaIds: List<Long>?,
        sensitive: Boolean,
        spoilerText: String?,
        visibility: Status.Visibility
    ): Status {
        return Statuses(client).postStatus(
            status = status,
            inReplyToId = inReplyToId,
            mediaIds = mediaIds,
            sensitive = sensitive,
            spoilerText = spoilerText,
            visibility = visibility
        ).execute()
    }

    override fun getStatus(id: Long): Status {
        return Statuses(client).getStatus(id).execute()
    }

    override fun postFavorite(id: Long): Status {
        return Statuses(client).postFavourite(id).execute()
    }

    override fun postUnfavorite(id: Long): Status {
        return Statuses(client).postUnfavourite(id).execute()
    }

    override fun postReblog(id: Long): Status {
        return Statuses(client).postReblog(id).execute()
    }
}
