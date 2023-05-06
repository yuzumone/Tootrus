package net.yuzumone.tootrus.ui.common

import android.view.View
import com.sys1yagi.mastodon4j.api.entity.Status

interface OnStatusAdapterSingleClickListener {
    fun onClick(view: View, status: Status)
}
