package net.yuzumone.tootrus.data.mastodon

import com.sys1yagi.mastodon4j.MastodonClient
import com.sys1yagi.mastodon4j.api.Range
import com.sys1yagi.mastodon4j.api.entity.Status
import com.sys1yagi.mastodon4j.api.method.Public
import javax.inject.Inject
import javax.inject.Named

interface PublicRepository {
    fun getLocalPublic(range: Range):List<Status>
}

class DefaultPublicRepository @Inject constructor(
        @Named("client") private val client: MastodonClient
) : PublicRepository {
    override fun getLocalPublic(range: Range): List<Status> {
        val result = Public(client).getLocalPublic(range).execute()
        return result.part
    }
}