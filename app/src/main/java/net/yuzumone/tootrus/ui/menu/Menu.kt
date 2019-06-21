package net.yuzumone.tootrus.ui.menu

import com.sys1yagi.mastodon4j.api.entity.Status

data class Menu(
        val status: Status? = null,
        val title: String = "",
        val accountId: Long = 0L,
        val statusUrl: String = "",
        val action: String = "") {
    enum class Action(val value: String) {
        Account("account"),
        Share("share"),
        Conversation("conversation")
    }
}