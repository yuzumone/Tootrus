package net.yuzumone.tootrus.ui.common

import android.content.Context
import androidx.databinding.DataBindingUtil
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.sys1yagi.mastodon4j.api.entity.Attachment
import net.yuzumone.tootrus.R
import net.yuzumone.tootrus.databinding.ViewThumbnailBinding
import net.yuzumone.tootrus.ui.MediaPreviewActivity

class ThumbnailView : LinearLayout {

    constructor(context: Context?) :
            this(context, null)

    constructor(context: Context?, attrs: AttributeSet?) :
            this(context, attrs, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
            this(context, attrs, defStyleAttr, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) :
            super(context, attrs, defStyleAttr, defStyleRes) {
        orientation = HORIZONTAL
    }

    fun setAttachments(attachments: List<Attachment>) {
        attachments.forEachIndexed { index, attachment ->
            val v = bindView(attachment)
            v.setOnClickListener { _ ->
                val media = MediaPreviewActivity.Media(attachments, index)
                val intent = MediaPreviewActivity.createIntent(context, media)
                context.startActivity(intent)
            }
            addView(v)
        }
    }

    fun clearThumbnail() {
        removeAllViews()
    }

    private fun bindView(attachment: Attachment): View {
        val binding = DataBindingUtil.inflate<ViewThumbnailBinding>(
                LayoutInflater.from(context), R.layout.view_thumbnail, this, false
        ).also {
            it.attachment = attachment
        }
        return binding.root
    }
}