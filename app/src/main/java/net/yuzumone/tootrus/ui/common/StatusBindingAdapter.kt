package net.yuzumone.tootrus.ui.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.sys1yagi.mastodon4j.api.entity.Status
import net.yuzumone.tootrus.R
import net.yuzumone.tootrus.databinding.ItemStatusBinding

class StatusBindingAdapter() : BindingRecyclerAdapter<Status, ItemStatusBinding>() {

    private var expandListener: OnStatusAdapterClickListener? = null
    private var singleListener: OnStatusAdapterSingleClickListener? = null

    constructor(listener: OnStatusAdapterClickListener) : this () {
        this.expandListener = listener
    }

    constructor(listener: OnStatusAdapterSingleClickListener) : this () {
        this.singleListener = listener
    }

    override fun createBinding(parent: ViewGroup): ItemStatusBinding {
        return ItemStatusBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun bind(binding: ItemStatusBinding, item: Status) {
        binding.also {
            it.viewQuick.visibility = View.GONE
            it.viewThumbnail.clearThumbnail()
            it.viewWebCard.clearCard()
            it.status = item
            it.expandListener = expandListener
            it.singleListener = singleListener
        }
        if (expandListener != null) {
            binding.singleListener = getViewExpandListener()
        }
    }

    private fun getViewExpandListener(): OnStatusAdapterSingleClickListener {
        return object : OnStatusAdapterSingleClickListener {
            override fun onClick(view: View, status: Status) {
                val anim = AnimationUtils.loadAnimation(view.context, R.anim.anim_view_quick)
                val v = view.findViewById<View>(R.id.view_quick)
                if (v.visibility == View.GONE) {
                    v.visibility = View.VISIBLE
                    v.startAnimation(anim)
                } else {
                    v.visibility = View.GONE
                }
            }
        }
    }
}