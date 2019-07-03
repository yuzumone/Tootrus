package net.yuzumone.tootrus.ui.menu

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.sys1yagi.mastodon4j.api.entity.Status
import net.yuzumone.tootrus.databinding.FragmentMenuDialogBinding
import net.yuzumone.tootrus.ui.ProfileActivity
import net.yuzumone.tootrus.ui.conversation.ConversationDialogFragment

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        menuViewModel = ViewModelProviders.of(this).get(MenuViewModel::class.java)
        adapter = MenuBindingAdapter(menuViewModel)
        binding = FragmentMenuDialogBinding.inflate(inflater, container, false).also {
            it.recyclerMenu.adapter = adapter
            it.recyclerMenu.layoutManager = LinearLayoutManager(activity)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        menuViewModel.setStatus(status)
        menuViewModel.menuList.observe(viewLifecycleOwner, Observer {
            adapter.update(it)
        })
        menuViewModel.menu.observe(viewLifecycleOwner, Observer {
            dismiss()
            when (it!!.action) {
                Menu.Action.Account.value -> {
                    requireActivity().run {
                        val intent = ProfileActivity.createIntent(this, it.accountId)
                        startActivity(intent)
                    }
                }
                Menu.Action.Conversation.value -> {
                    val dialog = ConversationDialogFragment.newReplyInstance(it.status!!)
                    dialog.show(requireFragmentManager(), "conversation")
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
                Menu.Action.COPY_LINK.value -> {
                    requireActivity().getSystemService(CLIPBOARD_SERVICE).let { m ->
                        (m as ClipboardManager).primaryClip = ClipData.newPlainText("", it.statusUrl)
                    }
                }
            }
        })
    }
}