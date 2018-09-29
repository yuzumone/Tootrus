package net.yuzumone.tootrus.ui.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import net.yuzumone.tootrus.R
import net.yuzumone.tootrus.databinding.ItemStatusBinding
import net.yuzumone.tootrus.vo.TootrusStatus

class StatusBindingAdapter(
        private val favoriteButtonClickCallback: ((TootrusStatus) -> Unit)?
) : BindingRecyclerAdapter<TootrusStatus, ItemStatusBinding>() {
    override fun createBinding(parent: ViewGroup): ItemStatusBinding {
        val binding = ItemStatusBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        return binding
    }

    override fun bind(binding: ItemStatusBinding, item: TootrusStatus) {
        binding.viewQuick.visibility = View.GONE
        binding.viewThumbnail.clearThumbnail()
        val anim = AnimationUtils.loadAnimation(binding.root.context, R.anim.anim_view_quick)
        binding.viewItem.setOnClickListener {
            if (binding.viewQuick.visibility == View.GONE) {
                binding.viewQuick.visibility = View.VISIBLE
                binding.viewQuick.startAnimation(anim)
            } else {
                binding.viewQuick.visibility = View.GONE
            }
        }
        binding.buttonFav.setOnClickListener {
            binding.status?.let { favoriteButtonClickCallback?.invoke(it) }
            binding.viewQuick.visibility = View.GONE
        }
        binding.status = item
    }
}