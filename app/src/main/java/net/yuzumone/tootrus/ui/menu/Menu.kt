package net.yuzumone.tootrus.ui.menu

data class Menu(
        val title: String = "",
        val accountId: Long = 0L,
        val statusUrl: String = "",
        val action: String = "") {
    enum class Action(val value: String) {
        Account("account"),
        Share("share")
    }
}