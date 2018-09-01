package net.yuzumone.tootrus.ui.common

import android.view.LayoutInflater
import android.view.ViewGroup
import com.sys1yagi.mastodon4j.api.entity.Status
import net.yuzumone.tootrus.databinding.ItemStatusBinding

class StatusBindingAdapter : BindingRecyclerAdapter<Status, ItemStatusBinding>() {
    override fun createBinding(parent: ViewGroup): ItemStatusBinding {
        val binding = ItemStatusBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        return binding
    }

    override fun bind(binding: ItemStatusBinding, item: Status) {
        binding.status = item
    }
}