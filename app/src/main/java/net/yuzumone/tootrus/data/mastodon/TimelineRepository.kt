package net.yuzumone.tootrus.data.mastodon

import com.sys1yagi.mastodon4j.MastodonClient
import com.sys1yagi.mastodon4j.api.Range
import com.sys1yagi.mastodon4j.api.entity.Status
import com.sys1yagi.mastodon4j.api.method.Timelines
import javax.inject.Inject

interface TimelineRepository {
    fun getTimeline(range: Range): List<Status>
}

class DefaultTimelineRepository @Inject constructor(
        private val client: MastodonClient
): TimelineRepository {
    override fun getTimeline(range: Range): List<Status> {
        val result = Timelines(client).getHome().execute()
        return result.part
    }
}