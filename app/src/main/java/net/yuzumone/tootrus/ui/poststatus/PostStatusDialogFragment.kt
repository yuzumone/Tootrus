package net.yuzumone.tootrus.ui.poststatus

import android.app.Dialog
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.DialogInterface
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import com.sys1yagi.mastodon4j.api.entity.Status
import dagger.android.support.AndroidSupportInjection
import net.yuzumone.tootrus.R
import net.yuzumone.tootrus.databinding.FragmentPostStatusBinding
import net.yuzumone.tootrus.ui.PostStatusViewModel
import javax.inject.Inject

class PostStatusDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentPostStatusBinding
    private lateinit var postStatusViewModel: PostStatusViewModel
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(activity!!)
        binding = DataBindingUtil.inflate(LayoutInflater.from(activity),
                R.layout.fragment_post_status, null, false)
        dialog.setContentView(binding.root)
        dialog.window?.apply {
            setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
        postStatusViewModel = ViewModelProviders.of(activity!!, viewModelFactory)
                .get(PostStatusViewModel::class.java)
        initializeToolbar()
        return dialog
    }

    private fun initializeToolbar() {
        binding.toolbar.setNavigationIcon(R.drawable.ic_action_back)
        binding.toolbar.setNavigationOnClickListener {
            dismiss()
        }
        binding.toolbar.inflateMenu(R.menu.menu_post_status)
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_post_status -> {
                    val text = binding.inputText.text.toString()
                    postStatus(text, null, null, false, null)
                    dismiss()
                }
            }
            false
        }
    }

    private fun postStatus(text: String,
                           inReplyToId: Long?,
                           mediaIds: List<Long>?,
                           sensitive: Boolean,
                           spoilerText: String?,
                           visibility: Status.Visibility = Status.Visibility.Public) {
        postStatusViewModel.postStatus(text, inReplyToId, mediaIds, sensitive, spoilerText, visibility)
    }

    override fun onDismiss(dialog: DialogInterface?) {
        requireActivity().finish()
        super.onDismiss(dialog)
    }
}