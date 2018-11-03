package net.yuzumone.tootrus.ui.common

import com.sys1yagi.mastodon4j.api.entity.Status

interface OnStatusAdapterClickListener {
    fun actionDetail(status: Status)
    fun actionReply(status: Status)
    fun actionFavorite(status: Status)
    fun actionReblog(status: Status)
    fun actionMenu(status: Status)
}