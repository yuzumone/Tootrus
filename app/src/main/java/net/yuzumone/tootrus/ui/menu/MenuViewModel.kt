package net.yuzumone.tootrus.ui.menu

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MenuViewModel : ViewModel(), OnMenuClickListener {

    val menu = MutableLiveData<Menu>()

    override fun action(menu: Menu) {
        this.menu.value = menu
    }
}