package net.yuzumone.tootrus.util

import android.view.View
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import androidx.databinding.BindingAdapter
import com.sys1yagi.mastodon4j.api.entity.Status

@BindingAdapter("navigation_click")
fun Toolbar.onNavigationClick(clickListener: View.OnClickListener) =
    setNavigationOnClickListener(clickListener)

@BindingAdapter("initialize_reply")
fun EditText.setReplyText(status: Status?) {
    status ?: return
    val text = "@${status.account?.acct} "
    setText(text)
    setSelection(text.length)
}
