package net.yuzumone.tootrus.data.mastodon

import com.google.gson.Gson
import com.sys1yagi.mastodon4j.MastodonClient
import com.sys1yagi.mastodon4j.api.entity.Status
import com.sys1yagi.mastodon4j.api.method.Timelines
import okhttp3.OkHttpClient
import javax.inject.Inject

interface TimelineRepository {
    fun getTimeline(instanceName: String, token: String): List<Status>
}

class DefaultTimelineRepository @Inject constructor(
        private val okHttpClientBuilder: OkHttpClient.Builder,
        private val gson: Gson
): TimelineRepository {
    override fun getTimeline(instanceName: String, token: String): List<Status> {
        val client = MastodonClient
                .Builder(instanceName, okHttpClientBuilder, gson)
                .accessToken(token).build()
        val result = Timelines(client).getHome().execute()
        return result.part
    }
}