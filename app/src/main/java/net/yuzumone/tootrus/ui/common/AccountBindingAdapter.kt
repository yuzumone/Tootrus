package net.yuzumone.tootrus.ui.common

import android.view.LayoutInflater
import android.view.ViewGroup
import com.sys1yagi.mastodon4j.api.entity.Account
import net.yuzumone.tootrus.databinding.ItemAccountBinding

class AccountBindingAdapter() : BindingRecyclerAdapter<Account, ItemAccountBinding>() {

    override fun createBinding(parent: ViewGroup): ItemAccountBinding {
        return ItemAccountBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun bind(binding: ItemAccountBinding, item: Account) {
        binding.account = item
    }
}