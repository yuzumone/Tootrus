package net.yuzumone.tootrus.ui.common

import com.sys1yagi.mastodon4j.api.entity.Status

interface OnStatusAdapterLongClickListener {
    fun onLongClick(status: Status): Boolean
}