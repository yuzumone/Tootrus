package net.yuzumone.tootrus.ui.menu

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sys1yagi.mastodon4j.api.entity.Status

class MenuViewModel : ViewModel(), OnMenuClickListener {

    val menuList = MutableLiveData<List<Menu>>()
    val menu = MutableLiveData<Menu>()

    override fun action(menu: Menu) {
        this.menu.value = menu
    }

    fun setStatus(s: Status) {
        menuList.value = createMenu(s)
    }

    private fun createMenu(status: Status): List<Menu> {
        val menuList = ArrayList<Menu>()
        menuList.add(
            Menu(
                title = status.account!!.userName,
                accountId = status.account!!.id,
                action = "account"
            )
        )
        status.reblog?.let {
            val menu =
                Menu(title = it.account!!.userName, accountId = it.account!!.id, action = "account")
            if (!menuList.contains(menu)) {
                menuList.add(menu)
            }
        }
        status.mentions.forEach {
            val menu = Menu(title = it.username, accountId = it.id, action = "account")
            if (!menuList.contains(menu)) {
                menuList.add(Menu(title = it.username, accountId = it.id, action = "account"))
            }
        }
        if (status.inReplyToId != null) {
            menuList.add(Menu(title = "Conversation", status = status, action = "conversation"))
        }
        menuList.add(Menu(title = "Share", statusUrl = status.url, action = "share"))
        menuList.add(
            Menu(
                title = "Copy link to toot",
                statusUrl = status.url,
                action = "copy_link"
            )
        )
        return menuList
    }
}
