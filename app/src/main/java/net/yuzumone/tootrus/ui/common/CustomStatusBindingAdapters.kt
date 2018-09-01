package net.yuzumone.tootrus.ui.common

import android.databinding.BindingAdapter
import android.text.Html
import android.widget.TextView
import com.facebook.drawee.view.SimpleDraweeView
import net.yuzumone.tootrus.R
import java.text.SimpleDateFormat
import java.util.*

object CustomStatusBindingAdapters {

    @BindingAdapter("icon")
    @JvmStatic
    fun setIcon(view: SimpleDraweeView, avatar: String) {
        view.setImageURI(avatar)
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
    fun setContent(view: TextView, content: String) {
        view.text = Html.fromHtml(content, Html.TO_HTML_PARAGRAPH_LINES_INDIVIDUAL)
    }

    @BindingAdapter("boosted_by")
    @JvmStatic
    fun setBoostedBy(view: TextView, userName: String) {
        view.text = view.context.getString(R.string.boosted_by, userName)
    }
}