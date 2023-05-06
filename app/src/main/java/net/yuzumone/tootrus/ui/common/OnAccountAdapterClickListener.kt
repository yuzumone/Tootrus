package net.yuzumone.tootrus.ui.common

import com.sys1yagi.mastodon4j.api.entity.Account

interface OnAccountAdapterClickListener {
    fun actionOpenAccount(account: Account)
}
