package net.yuzumone.tootrus.ui.common

import android.view.LayoutInflater
import android.view.ViewGroup
import net.yuzumone.tootrus.databinding.ItemNotificationBinding
import net.yuzumone.tootrus.vo.TootrusNotification

class NotificationBindingAdapter : BindingRecyclerAdapter<TootrusNotification, ItemNotificationBinding>() {
    override fun createBinding(parent: ViewGroup): ItemNotificationBinding {
        val binding = ItemNotificationBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        return binding
    }

    override fun bind(binding: ItemNotificationBinding, item: TootrusNotification) {
        binding.notification = item
    }
}