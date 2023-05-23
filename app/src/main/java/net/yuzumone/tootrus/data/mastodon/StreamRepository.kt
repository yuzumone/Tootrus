package net.yuzumone.tootrus.data.mastodon

import com.sys1yagi.mastodon4j.MastodonClient
import com.sys1yagi.mastodon4j.api.Handler
import com.sys1yagi.mastodon4j.api.Shutdownable
import com.sys1yagi.mastodon4j.api.method.Streaming
import net.yuzumone.tootrus.di.InterceptorMastodonStreamingClient
import javax.inject.Inject

interface StreamRepository {
    fun user(handler: Handler)
    fun shutdown()
}

class DefaultStreamRepository @Inject constructor(
    @InterceptorMastodonStreamingClient private val client: MastodonClient
) : StreamRepository {

    private lateinit var shutdownable: Shutdownable

    override fun user(handler: Handler) {
        val streaming = Streaming(client)
        shutdownable = streaming.user(handler)
    }

    override fun shutdown() {
        shutdownable.run { shutdown() }
    }
}
