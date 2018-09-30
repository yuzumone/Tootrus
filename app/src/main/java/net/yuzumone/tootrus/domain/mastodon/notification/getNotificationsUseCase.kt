package net.yuzumone.tootrus.domain.mastodon.notification

import com.sys1yagi.mastodon4j.api.Range
import net.yuzumone.tootrus.data.mastodon.NotificationRepository
import net.yuzumone.tootrus.domain.UseCase
import net.yuzumone.tootrus.vo.TootrusNotification
import javax.inject.Inject

class getNotificationsUseCase @Inject constructor(
        private val repository: NotificationRepository
) : UseCase<Range, List<TootrusNotification>>() {
    override suspend fun run(params: Range) = repository.getNotifications(params)
}