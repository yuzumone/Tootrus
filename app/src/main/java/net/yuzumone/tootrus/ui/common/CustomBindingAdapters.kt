package net.yuzumone.tootrus.ui.common

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ImageSpan
import android.view.View
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import com.facebook.common.executors.UiThreadImmediateExecutorService
import com.facebook.common.references.CloseableReference
import com.facebook.datasource.DataSource
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber
import com.facebook.imagepipeline.image.CloseableImage
import com.facebook.imagepipeline.request.ImageRequest
import com.sys1yagi.mastodon4j.api.entity.*
import me.relex.photodraweeview.PhotoDraweeView
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

    @BindingAdapter("photo_url")
    @JvmStatic
    fun setPhotoView(view: PhotoDraweeView, url: String) {
        view.setPhotoUri(Uri.parse(url))
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
    fun setContent(view: TextView, status: Status?) {
        status ?: return
        if (status.spoilerText.isNotEmpty()) {
            view.text = view.context.getString(R.string.spoiler_text, status.spoilerText)
            return
        }
        val doc = Jsoup.parse(status.content)
        doc.select("span.invisible").remove()
        doc.select("span.ellipsis").append("…")
        doc.select("p").append("\\n\\n")
        doc.select("br").append("\\n")
        val text = doc.text().replace("\\\\n\\s".toRegex(), "\\\\n")
                .replace("\\\\n\\\\n$".toRegex(), "")
                .split("\\n").joinToString("\n")
        view.text = text
        val sb = SpannableStringBuilder(text)
        status.emojis.forEach { emoji ->
            val shortCode = ":${emoji.shortcode}:"
            val request = ImageRequest.fromUri(emoji.url)
            val imagePipeline = Fresco.getImagePipeline()
            val dataSource = imagePipeline.fetchDecodedImage(request, null)
            dataSource.subscribe(object : BaseBitmapDataSubscriber() {
                override fun onFailureImpl(dataSource: DataSource<CloseableReference<CloseableImage>>?) {

                }

                override fun onNewResultImpl(bitmap: Bitmap?) {
                    bitmap ?: return
                    val lineHeight = view.lineHeight
                    val drawable = BitmapDrawable(view.resources, bitmap)
                    drawable.setBounds(0, 0, lineHeight, lineHeight)
                    var i = 0
                    while (text.indexOf(shortCode, i) != -1) {
                        val index = text.indexOf(shortCode, i)
                        val imageSpan = ImageSpan(drawable)
                        sb.setSpan(imageSpan, index, index + shortCode.length, Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
                        view.text = sb
                        i += index + 1
                    }
                }
            }, UiThreadImmediateExecutorService.getInstance())
        }
    }

    @BindingAdapter("detail_content")
    @JvmStatic
    fun setDetailContent(view: TextView, status: Status?) {
        status ?: return
        val doc = Jsoup.parse(status.content)
        doc.select("span.invisible").remove()
        doc.select("span.ellipsis").append("…")
        doc.select("p").append("\\n\\n")
        doc.select("br").append("\\n")
        var text = doc.text().replace("\\\\n\\s".toRegex(), "\\\\n")
                .replace("\\\\n\\\\n$".toRegex(), "")
                .split("\\n").joinToString("\n")
        if (status.spoilerText.isNotEmpty()) {
            text = "${status.spoilerText}\n\n$text"
        }
        view.text = text
        val sb = SpannableStringBuilder(text)
        status.emojis.forEach { emoji ->
            val shortCode = ":${emoji.shortcode}:"
            val request = ImageRequest.fromUri(emoji.url)
            val imagePipeline = Fresco.getImagePipeline()
            val dataSource = imagePipeline.fetchDecodedImage(request, null)
            dataSource.subscribe(object : BaseBitmapDataSubscriber() {
                override fun onFailureImpl(dataSource: DataSource<CloseableReference<CloseableImage>>?) {

                }

                override fun onNewResultImpl(bitmap: Bitmap?) {
                    bitmap ?: return
                    val lineHeight = view.lineHeight
                    val drawable = BitmapDrawable(view.resources, bitmap)
                    drawable.setBounds(0, 0, lineHeight, lineHeight)
                    var i = 0
                    while (text.indexOf(shortCode, i) != -1) {
                        val index = text.indexOf(shortCode, i)
                        val imageSpan = ImageSpan(drawable)
                        sb.setSpan(imageSpan, index, index + shortCode.length, Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
                        view.text = sb
                        i += index + 1
                    }
                }
            }, UiThreadImmediateExecutorService.getInstance())
        }
    }

    @BindingAdapter("note")
    @JvmStatic
    fun setAccountNote(view: TextView, account: Account?) {
        account ?: return
        val doc = Jsoup.parse(account.note)
        doc.select("span.invisible").remove()
        doc.select("span.ellipsis").append("…")
        doc.select("p").append("\\n\\n")
        doc.select("br").append("\\n")
        val text = doc.text().replace("\\\\n\\s".toRegex(), "\\\\n")
                .replace("\\\\n\\\\n$".toRegex(), "")
                .split("\\n").joinToString("\n")
        view.text = text
        val sb = SpannableStringBuilder(text)
        account.emojis.forEach { emoji ->
            val shortCode = ":${emoji.shortcode}:"
            val request = ImageRequest.fromUri(emoji.url)
            val imagePipeline = Fresco.getImagePipeline()
            val dataSource = imagePipeline.fetchDecodedImage(request, null)
            dataSource.subscribe(object : BaseBitmapDataSubscriber() {
                override fun onFailureImpl(dataSource: DataSource<CloseableReference<CloseableImage>>?) {

                }

                override fun onNewResultImpl(bitmap: Bitmap?) {
                    bitmap ?: return
                    val lineHeight = view.lineHeight
                    val drawable = BitmapDrawable(view.resources, bitmap)
                    drawable.setBounds(0, 0, lineHeight, lineHeight)
                    var i = 0
                    while (text.indexOf(shortCode, i) != -1) {
                        val index = text.indexOf(shortCode, i)
                        val imageSpan = ImageSpan(drawable)
                        sb.setSpan(imageSpan, index, index + shortCode.length, Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
                        view.text = sb
                        i += index + 1
                    }
                }
            }, UiThreadImmediateExecutorService.getInstance())
        }
    }

    @BindingAdapter("web_card")
    @JvmStatic
    fun setWebCard(view: WebCardView, content: String?) {
        view.setContent(content)
    }

    @BindingAdapter("web_card_visibility")
    @JvmStatic
    fun setWebCardViewVisibility(view: WebCardView, content: String?) {
        view.visibility = View.GONE
        val card = Jsoup.parse(content ?: "").select("a")
                .filter { !it.attr("class").contains("mention") }
        if (card.isNotEmpty()) {
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

    @BindingAdapter("status_visibility")
    @JvmStatic
    fun setStatusVisibility(view: TextView, visibility: String) {
        val lineHeight = view.lineHeight
        when (visibility) {
            Status.Visibility.Direct.value -> {
                ResourcesCompat.getDrawable(view.resources, R.drawable.ic_item_direct, null)?.let {
                    it.setBounds(0, 0, lineHeight, lineHeight)
                    view.setCompoundDrawables(it, null, null, null)
                }
            }
            Status.Visibility.Private.value -> {
                ResourcesCompat.getDrawable(view.resources, R.drawable.ic_item_private, null)?.let {
                    it.setBounds(0, 0, lineHeight, lineHeight)
                    view.setCompoundDrawables(it, null, null, null)
                }
            }
            Status.Visibility.Unlisted.value -> {
                ResourcesCompat.getDrawable(view.resources, R.drawable.ic_item_unlisted, null)?.let {
                    it.setBounds(0, 0, lineHeight, lineHeight)
                    view.setCompoundDrawables(it, null, null, null)
                }
            }
            else -> {
                view.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0)
            }
        }
    }

    @BindingAdapter("image_uris")
    @JvmStatic
    fun setImageUris(view: PostImageView, uris: List<String>?) {
        view.update(uris)
    }
}