package net.yuzumone.tootrus.domain.mastodon.notification

import com.sys1yagi.mastodon4j.api.Range
import com.sys1yagi.mastodon4j.api.entity.Notification
import net.yuzumone.tootrus.data.mastodon.NotificationRepository
import net.yuzumone.tootrus.domain.UseCase
import javax.inject.Inject

class GetNotificationsUseCase @Inject constructor(
    private val repository: NotificationRepository
) : UseCase<Range, List<Notification>>() {
    override suspend fun run(params: Range) = repository.getNotifications(params)
}
