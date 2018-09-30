package net.yuzumone.tootrus.vo

import com.sys1yagi.mastodon4j.api.entity.Account
import com.sys1yagi.mastodon4j.api.entity.Notification
import java.io.Serializable

data class TootrusNotification(
        val id: Long = 0L,
        val type: String = "",
        val createdAt: String = "",
        val account: Account? = null,
        var status: TootrusStatus? = null
) : Serializable {

    constructor(notification: Notification) : this(
            id = notification.id,
            type = notification.type,
            createdAt = notification.createdAt,
            account = notification.account
    ) {
        if (notification.status != null) {
            this.status = TootrusStatus(notification.status!!)
        }
    }

    enum class Type(val value: String) {
        Mention("mention"),
        Reblog("reblog"),
        Favourite("favourite"),
        Follow("follow")
    }
}