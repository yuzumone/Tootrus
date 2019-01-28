package net.yuzumone.tootrus.ui.common

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class BindingHolder<out T : ViewDataBinding>(val binding: T) :
        RecyclerView.ViewHolder(binding.root)