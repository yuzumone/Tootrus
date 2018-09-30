package net.yuzumone.tootrus.data.mastodon

import com.sys1yagi.mastodon4j.MastodonClient
import com.sys1yagi.mastodon4j.api.Range
import com.sys1yagi.mastodon4j.api.method.Notifications
import net.yuzumone.tootrus.vo.TootrusNotification
import javax.inject.Inject
import javax.inject.Named

interface NotificationRepository {
    fun getNotifications(range: Range): List<TootrusNotification>
}

class DefaultNotificationRepository @Inject constructor(
        @Named("client") private val client: MastodonClient
) : NotificationRepository {
    override fun getNotifications(range: Range): List<TootrusNotification> {
        val result = Notifications(client).getNotifications(range).execute()
        return result.part.map { TootrusNotification(it) }
    }
}