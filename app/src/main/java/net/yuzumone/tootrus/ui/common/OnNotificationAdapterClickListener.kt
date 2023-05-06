package net.yuzumone.tootrus.ui.common

import com.sys1yagi.mastodon4j.api.entity.Notification

interface OnNotificationAdapterClickListener {
    fun onSingleClick(notification: Notification)
}
