package net.yuzumone.tootrus.ui.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.sys1yagi.mastodon4j.api.entity.Status
import net.yuzumone.tootrus.R
import net.yuzumone.tootrus.databinding.ItemStatusBinding

class StatusBindingAdapter(
        private val listener: OnStatusAdapterClickListener
) : BindingRecyclerAdapter<Status, ItemStatusBinding>() {
    override fun createBinding(parent: ViewGroup): ItemStatusBinding {
        return ItemStatusBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun bind(binding: ItemStatusBinding, item: Status) {
        binding.viewQuick.visibility = View.GONE
        binding.viewThumbnail.clearThumbnail()
        val anim = AnimationUtils.loadAnimation(binding.root.context, R.anim.anim_view_quick)
        binding.root.setOnClickListener {
            if (binding.viewQuick.visibility == View.GONE) {
                binding.viewQuick.visibility = View.VISIBLE
                binding.viewQuick.startAnimation(anim)
            } else {
                binding.viewQuick.visibility = View.GONE
            }
        }
        binding.status = item
        binding.listener = listener
    }
}