package net.yuzumone.tootrus.data.mastodon

import com.sys1yagi.mastodon4j.MastodonClient
import com.sys1yagi.mastodon4j.api.Range
import com.sys1yagi.mastodon4j.api.entity.Notification
import com.sys1yagi.mastodon4j.api.method.Notifications
import net.yuzumone.tootrus.di.InterceptorMastodonClient
import javax.inject.Inject

interface NotificationRepository {
    fun getNotifications(range: Range): List<Notification>
}

class DefaultNotificationRepository @Inject constructor(
    @InterceptorMastodonClient private val client: MastodonClient
) : NotificationRepository {
    override fun getNotifications(range: Range): List<Notification> {
        val result = Notifications(client).getNotifications(range).execute()
        return result.part
    }
}
