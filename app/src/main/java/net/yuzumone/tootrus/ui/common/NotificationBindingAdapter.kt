package net.yuzumone.tootrus.ui.common

import android.view.LayoutInflater
import android.view.ViewGroup
import com.sys1yagi.mastodon4j.api.entity.Notification
import net.yuzumone.tootrus.databinding.ItemNotificationBinding

class NotificationBindingAdapter(
        val listener: OnNotificationAdapterClickListener
) : BindingRecyclerAdapter<Notification, ItemNotificationBinding>() {
    override fun createBinding(parent: ViewGroup): ItemNotificationBinding {
        val binding = ItemNotificationBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        return binding
    }

    override fun bind(binding: ItemNotificationBinding, item: Notification) {
        binding.notification = item
        binding.listener = listener
    }
}