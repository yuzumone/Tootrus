package net.yuzumone.tootrus.ui.menu

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.sys1yagi.mastodon4j.api.entity.Status
import net.yuzumone.tootrus.databinding.FragmentMenuDialogBinding
import net.yuzumone.tootrus.ui.ProfileActivity

class MenuDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentMenuDialogBinding
    private lateinit var menuViewModel: MenuViewModel
    private lateinit var adapter: MenuBindingAdapter
    private val status by lazy { Gson().fromJson(arguments!!.getString(ARG_STATUS), Status::class.java) }

    companion object {
        private const val ARG_STATUS = "status"
        fun newInstance(status: Status): MenuDialogFragment {
            return MenuDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_STATUS, Gson().toJson(status))
                }
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(activity!!)
        menuViewModel = ViewModelProviders.of(this).get(MenuViewModel::class.java)
        adapter = MenuBindingAdapter(menuViewModel)
        binding = FragmentMenuDialogBinding.inflate(LayoutInflater.from(activity),
                null, false).apply {
            recyclerMenu.adapter = adapter
            recyclerMenu.layoutManager = LinearLayoutManager(activity)
        }
        dialog.setContentView(binding.root)
        createMenu()
        menuViewModel.menu.observe(this, Observer {
            dismiss()
            when (it!!.action) {
                Menu.Action.Account.value -> {
                    requireActivity().run {
                        val intent = ProfileActivity.createIntent(this, it.accountId)
                        startActivity(intent)
                    }
                }
                Menu.Action.Share.value -> {
                    requireActivity().run {
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, it.statusUrl)
                        }
                        startActivity(intent)
                    }
                }
            }
        })
        return dialog
    }

    private fun createMenu() {
        val menuList = ArrayList<Menu>()
        menuList.add(Menu(title = status.account!!.userName, accountId = status.account!!.id, action = "account"))
        status.reblog?.let {
            menuList.add(Menu(title = it.account!!.userName, accountId = it.account!!.id, action = "account"))
        }
        status.mentions.forEach {
            menuList.add(Menu(title = it.username, accountId = it.id, action = "account"))
        }
        menuList.add(Menu(title = "Share", statusUrl = status.url, action = "share"))
        adapter.update(menuList)
    }
}