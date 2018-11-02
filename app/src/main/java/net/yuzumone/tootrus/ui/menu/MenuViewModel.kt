package net.yuzumone.tootrus.ui.menu

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

class MenuViewModel : ViewModel(), OnMenuClickListener {

    val menu = MutableLiveData<Menu>()

    override fun action(menu: Menu) {
        this.menu.value = menu
    }
}