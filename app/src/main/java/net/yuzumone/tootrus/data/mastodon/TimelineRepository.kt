package net.yuzumone.tootrus.data.mastodon

import com.sys1yagi.mastodon4j.MastodonClient
import com.sys1yagi.mastodon4j.api.Range
import com.sys1yagi.mastodon4j.api.method.Timelines
import net.yuzumone.tootrus.vo.TootrusStatus
import javax.inject.Inject
import javax.inject.Named

interface TimelineRepository {
    fun getTimeline(range: Range): List<TootrusStatus>
}

class DefaultTimelineRepository @Inject constructor(
        @Named("client") private val client: MastodonClient
): TimelineRepository {
    override fun getTimeline(range: Range): List<TootrusStatus> {
        val result = Timelines(client).getHome().execute()
        return result.part.map { TootrusStatus(it) }
    }
}