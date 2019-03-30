package net.yuzumone.tootrus.ui.poststatus

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.sys1yagi.mastodon4j.api.entity.Status
import net.yuzumone.tootrus.R
import net.yuzumone.tootrus.databinding.FragmentPostStatusBinding
import net.yuzumone.tootrus.domain.mastodon.status.PostStatusParams
import net.yuzumone.tootrus.service.PostStatusService

class PostStatusDialogFragment : DialogFragment() {

    private var visibility: Status.Visibility = Status.Visibility.Public
    private lateinit var binding: FragmentPostStatusBinding
    private lateinit var viewModel: PostStatusDialogViewModel
    private val inReplyToAcct: String? by lazy { arguments?.getString(ARG_IN_REPLY_TO_ACCT) }
    private val inReplyToId: Long? by lazy { arguments?.getLong(ARG_IN_REPLY_TO_ID) }

    companion object {
        private const val ARG_IN_REPLY_TO_ACCT = "in_reply_to_acct"
        private const val ARG_IN_REPLY_TO_ID = "in_reply_to_id"
        fun newReplyInstance(inReplyToAcct: String, inReplyToStatusId: Long): PostStatusDialogFragment {
            return PostStatusDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_IN_REPLY_TO_ACCT, inReplyToAcct)
                    putLong(ARG_IN_REPLY_TO_ID, inReplyToStatusId)
                }
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        viewModel = ViewModelProviders.of(this).get(PostStatusDialogViewModel::class.java)
        val dialog = Dialog(activity!!)
        binding = DataBindingUtil.inflate(LayoutInflater.from(activity),
                R.layout.fragment_post_status, null, false)
        binding.viewModel = viewModel
        dialog.setContentView(binding.root)
        dialog.window?.apply {
            setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
        initializeToolbar()
        if (inReplyToAcct != null) {
            val s = "@$inReplyToAcct "
            binding.inputText.setText(s)
            binding.inputText.setSelection(s.length)
        }

        viewModel.visibility.observe(this, Observer {
            visibility = it
            updateVisibilityMenu()
            if (binding.viewVisibility.visibility == View.VISIBLE) {
                binding.viewVisibility.visibility = View.GONE
            }
        })
        return dialog
    }

    private fun initializeToolbar() {
        binding.toolbar.setNavigationIcon(R.drawable.ic_action_back)
        binding.toolbar.setNavigationOnClickListener {
            dismiss()
        }
        binding.toolbar.inflateMenu(R.menu.menu_post_status)
        binding.toolbar.inflateMenu(R.menu.menu_visibility_public)
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_post_status -> {
                    val text = binding.inputText.text.toString()
                    postStatus(text, inReplyToId, null, false, null, visibility)
                    dismiss()
                }
                R.id.menu_visibility_public, R.id.menu_visibility_unlisted,
                R.id.menu_visibility_private, R.id.menu_visibility_direct -> {
                    val anim = AnimationUtils.loadAnimation(activity, R.anim.anim_view_quick)
                    if (binding.viewVisibility.visibility == View.GONE) {
                        binding.viewVisibility.visibility = View.VISIBLE
                        binding.viewVisibility.startAnimation(anim)
                    } else {
                        binding.viewVisibility.visibility = View.GONE
                    }
                }
            }
            false
        }
    }

    private fun updateVisibilityMenu() {
        binding.toolbar.menu.removeItem(R.id.menu_visibility_public)
        binding.toolbar.menu.removeItem(R.id.menu_visibility_unlisted)
        binding.toolbar.menu.removeItem(R.id.menu_visibility_private)
        binding.toolbar.menu.removeItem(R.id.menu_visibility_direct)
        when (visibility) {
            Status.Visibility.Public -> {
                binding.toolbar.inflateMenu(R.menu.menu_visibility_public)
            }
            Status.Visibility.Unlisted -> {
                binding.toolbar.inflateMenu(R.menu.menu_visibility_unlisted)
            }
            Status.Visibility.Private -> {
                binding.toolbar.inflateMenu(R.menu.menu_visibility_private)
            }
            Status.Visibility.Direct -> {
                binding.toolbar.inflateMenu(R.menu.menu_visibility_direct)
            }
        }
    }

    private fun postStatus(text: String,
                           inReplyToId: Long?,
                           mediaIds: List<Long>?,
                           sensitive: Boolean,
                           spoilerText: String?,
                           visibility: Status.Visibility = Status.Visibility.Public) {
        val params = PostStatusParams(text, inReplyToId, mediaIds, sensitive, spoilerText, visibility)
        requireActivity().run {
            val intent = PostStatusService.createIntent(this, params)
            startService(intent)
        }
    }

    override fun onDismiss(dialog: DialogInterface?) {
        requireActivity().finish()
        super.onDismiss(dialog)
    }
}