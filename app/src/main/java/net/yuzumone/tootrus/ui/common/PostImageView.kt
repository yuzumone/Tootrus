package net.yuzumone.tootrus.ui.common

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import net.yuzumone.tootrus.R
import net.yuzumone.tootrus.databinding.ViewPostImageBinding

class PostImageView : LinearLayout {

    private var listener: OnImageItemClickListener? = null

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

    fun update(uris: List<String>?) {
        removeAllViews()
        uris?.forEach { uri ->
            val v = bindView(uri)
            v.setOnClickListener {
                listener?.onClick(uri)
            }
            addView(v)
        }
    }

    fun setOnImageItemClickListener(listener: OnImageItemClickListener) {
        this.listener = listener
    }

    private fun bindView(uri: String): View {
        val binding = DataBindingUtil.inflate<ViewPostImageBinding>(
            LayoutInflater.from(context), R.layout.view_post_image, this, false
        ).also {
            it.uri = uri
        }
        return binding.root
    }

    interface OnImageItemClickListener {
        fun onClick(uri: String)
    }
}
