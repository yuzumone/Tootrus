package net.yuzumone.tootrus.ui.detail

import com.sys1yagi.mastodon4j.api.entity.Status

interface OnStatusDetailClickListener {
    fun actionReply(status: Status)
    fun actionFavorite(status: Status)
    fun actionReblog(status: Status)
    fun actionMenu(status: Status)
}