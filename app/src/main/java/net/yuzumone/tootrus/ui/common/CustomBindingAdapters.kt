package net.yuzumone.tootrus.ui.common

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.facebook.drawee.view.SimpleDraweeView
import com.sys1yagi.mastodon4j.api.entity.Attachment
import com.sys1yagi.mastodon4j.api.entity.Notification
import com.sys1yagi.mastodon4j.api.entity.Relationship
import com.sys1yagi.mastodon4j.api.entity.Status
import net.yuzumone.tootrus.R
import org.jsoup.Jsoup
import java.text.SimpleDateFormat
import java.util.*

object CustomBindingAdapters {

    @BindingAdapter("image_url")
    @JvmStatic
    fun setIcon(view: SimpleDraweeView, imageUrl: String?) {
        view.setImageURI(imageUrl)
    }

    @BindingAdapter("media_attachments")
    @JvmStatic
    fun setMediaAttachments(view: ThumbnailView, attachments: List<Attachment>) {
        view.setAttachments(attachments)
    }

    @BindingAdapter("thumbnail_visibility")
    @JvmStatic
    fun setThumbnailViewVisibility(view: ThumbnailView, status: Status) {
        if (status.reblog == null) {
            if (status.mediaAttachments.isEmpty()) {
                view.visibility = View.GONE
            } else {
                view.visibility = View.VISIBLE
            }
        } else {
            if (status.reblog!!.mediaAttachments.isEmpty()) {
                view.visibility = View.GONE
            } else {
                view.visibility = View.VISIBLE
            }
        }
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

    @BindingAdapter("absolute")
    @JvmStatic
    fun setAbsoluteDate(view: TextView, createAt: String?) {
        createAt ?: return
        val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.US)
                .parse(createAt)
        view.text = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.US).format(date)
    }

    @BindingAdapter("content")
    @JvmStatic
    fun setContent(view: TextView, content: String?) {
        val doc = Jsoup.parse(content ?: "")
        doc.select("span.invisible").remove()
        doc.select("span.ellipsis").append("…")
        doc.select("p").append("\\n\\n")
        doc.select("br").append("\\n")
        val text = doc.text().replace("\\\\n\\s".toRegex(), "\\\\n")
                .replace("\\\\n\\\\n$".toRegex(), "")
        view.text = text.split("\\n").joinToString("\n")
    }

    @BindingAdapter("web_card")
    @JvmStatic
    fun setWebCard(view: WebCardView, content: String?) {
        view.setContent(content)
    }

    @BindingAdapter("web_card_visibility")
    @JvmStatic
    fun setWebCardViewVisibility(view: WebCardView, content: String?) {
        if (content.isNullOrBlank() || Jsoup.parse(content).select("a").size == 0) {
            view.visibility = View.GONE
        } else {
            view.visibility = View.VISIBLE
        }
    }

    @BindingAdapter("boosted_by")
    @JvmStatic
    fun setBoostedBy(view: TextView, userName: String) {
        view.text = view.context.getString(R.string.boosted_by, userName)
    }

    @BindingAdapter("notification_label")
    @JvmStatic
    fun setNotificationLabel(view: TextView, notification: Notification) {
        val name = if (notification.account?.displayName!!.isEmpty())
            notification.account?.userName else notification.account?.displayName
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

    @BindingAdapter("relationship")
    @JvmStatic
    fun setRelationship(view: TextView, relationship: Relationship?) {
        relationship ?: return
        val text = if (relationship.isFollowedBy) "This user is following you." else "This user is not following you."
        view.text = text
    }
}