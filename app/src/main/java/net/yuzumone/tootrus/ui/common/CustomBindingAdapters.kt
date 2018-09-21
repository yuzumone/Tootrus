package net.yuzumone.tootrus.ui.common

import android.databinding.BindingAdapter
import android.text.Html
import android.widget.TextView
import com.facebook.drawee.view.SimpleDraweeView
import com.sys1yagi.mastodon4j.api.entity.Notification
import net.yuzumone.tootrus.R
import java.text.SimpleDateFormat
import java.util.*

object CustomBindingAdapters {

    @BindingAdapter("image_url")
    @JvmStatic
    fun setIcon(view: SimpleDraweeView, imageUrl: String) {
        view.setImageURI(imageUrl)
    }

    @BindingAdapter("created_at")
    @JvmStatic
    fun setRelativeDate(view: TextView, createAt: String) {
        val now = Date().time
        val tootTime = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.US)
                .parse(createAt).time
        var relative: String? = null
        var time = (now - tootTime) / 1000
        if (time <= 59) {
            relative = time.toString() + "s"
        }
        time /= 60
        if (time in 1..59) {
            relative = time.toString() + "m"
        }
        time /= 60
        if (time in 1..23) {
            relative = time.toString() + "h"
        }
        time /= 24
        if (time != 0L) {
            relative = time.toString() + "d"
        }
        view.text = relative
    }

    @BindingAdapter("content")
    @JvmStatic
    fun setContent(view: TextView, content: String?) {
        view.text = Html.fromHtml(content ?: "", Html.TO_HTML_PARAGRAPH_LINES_INDIVIDUAL)
    }

    @BindingAdapter("boosted_by")
    @JvmStatic
    fun setBoostedBy(view: TextView, userName: String) {
        view.text = view.context.getString(R.string.boosted_by, userName)
    }

    @BindingAdapter("notification_label")
    @JvmStatic
    fun setNotificationLabel(view: TextView, notification: Notification) {
        val name = notification.account?.displayName
        when (notification.type) {
            Notification.Type.Mention.value -> {
                view.text = ""
            }
            Notification.Type.Favourite.value -> {
                view.text = view.context.getString(R.string.favorited_status, name)
            }
            Notification.Type.Reblog.value -> {
                view.text = view.context.getString(R.string.boosted_status, name)
            }
            Notification.Type.Follow.value -> {
                view.text = view.context.getString(R.string.followed_you, name)
            }
        }
    }
}