package net.yuzumone.tootrus.ui.menu

import android.view.LayoutInflater
import android.view.ViewGroup
import net.yuzumone.tootrus.databinding.ItemMenuBinding
import net.yuzumone.tootrus.ui.common.BindingRecyclerAdapter

class MenuBindingAdapter(val listener: OnMenuClickListener) :
    BindingRecyclerAdapter<Menu, ItemMenuBinding>() {
    override fun createBinding(parent: ViewGroup): ItemMenuBinding {
        return ItemMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun bind(binding: ItemMenuBinding, item: Menu) {
        binding.menu = item
        binding.listener = listener
    }
}
