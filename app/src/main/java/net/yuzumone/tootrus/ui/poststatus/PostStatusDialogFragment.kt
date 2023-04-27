package net.yuzumone.tootrus.ui.poststatus

import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.view.animation.AnimationUtils
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.sys1yagi.mastodon4j.api.entity.Status
import net.yuzumone.tootrus.R
import net.yuzumone.tootrus.databinding.FragmentPostStatusBinding
import net.yuzumone.tootrus.service.PostStatusService
import net.yuzumone.tootrus.ui.common.PostImageView

class PostStatusDialogFragment : DialogFragment() {

    private var visibility = Status.Visibility.Public
    private var isSensitive = false
    private val imageUris = ArrayList<String>()
    private lateinit var binding: FragmentPostStatusBinding
    private lateinit var viewModel: PostStatusDialogViewModel
    private val repliedStatus: Status? by lazy {
        Gson().fromJson(arguments?.getString(ARG_REPLIED_STATUS), Status::class.java)
    }
    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        uri ?: return@registerForActivityResult
        imageUris.add(uri.toString())
        viewModel.setImageUris(imageUris)
    }

    companion object {
        private const val ARG_REPLIED_STATUS = "replied_status"
        fun newReplyInstance(status: Status): PostStatusDialogFragment {
            return PostStatusDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_REPLIED_STATUS, Gson().toJson(status))
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[PostStatusDialogViewModel::class.java]
        viewModel.setRepliedStatus(repliedStatus)
        binding = FragmentPostStatusBinding.inflate(inflater, container, false).also {
            it.viewModel = viewModel
            it.lifecycleOwner = viewLifecycleOwner
            it.viewPostImage.setOnImageItemClickListener(object :
                PostImageView.OnImageItemClickListener {
                override fun onClick(uri: String) {
                    imageUris.remove(uri)
                    viewModel.setImageUris(imageUris)
                }
            })
        }
        (requireActivity() as AppCompatActivity).also {
            it.setSupportActionBar(binding.toolbar)
            it.title = ""
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.also {
            it.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
            it.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
        viewModel.statusVisibility.observe(viewLifecycleOwner, Observer {
            visibility = it
            requireActivity().invalidateOptionsMenu()
            if (binding.viewVisibility.visibility == View.VISIBLE) {
                binding.viewVisibility.visibility = View.GONE
            }
        })
        viewModel.isSensitive.observe(viewLifecycleOwner, Observer {
            isSensitive = it
            requireActivity().invalidateOptionsMenu()
        })
        viewModel.eventNavigationClick.observe(viewLifecycleOwner, Observer {
            dismiss()
        })
        viewModel.draft.observe(viewLifecycleOwner, Observer {
            requireActivity().run {
                val intent = PostStatusService.createIntent(this, it)
                startService(intent)
            }
        })
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.let {
                    it.inflate(R.menu.menu_post_status, menu)
                    it.inflate(R.menu.menu_visibility_public, menu)
                    it.inflate(R.menu.menu_nsfw_to_on, menu)
                    it.inflate(R.menu.menu_content_warning, menu)
                }
                menu.let {
                    it.removeItem(R.id.menu_visibility_public)
                    it.removeItem(R.id.menu_visibility_unlisted)
                    it.removeItem(R.id.menu_visibility_private)
                    it.removeItem(R.id.menu_visibility_direct)
                    it.removeItem(R.id.menu_nsfw_to_on)
                    it.removeItem(R.id.menu_nsfw_to_off)
                }
                menuInflater.let {
                    when (visibility) {
                        Status.Visibility.Public -> {
                            it.inflate(R.menu.menu_visibility_public, menu)
                        }
                        Status.Visibility.Unlisted -> {
                            it.inflate(R.menu.menu_visibility_unlisted, menu)
                        }
                        Status.Visibility.Private -> {
                            it.inflate(R.menu.menu_visibility_private, menu)
                        }
                        Status.Visibility.Direct -> {
                            it.inflate(R.menu.menu_visibility_direct, menu)
                        }
                    }
                    if (isSensitive) {
                        it.inflate(R.menu.menu_nsfw_to_off, menu)
                    } else {
                        it.inflate(R.menu.menu_nsfw_to_on, menu)
                    }
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.menu_post_status -> {
                        viewModel.postStatus()
                        dismiss()
                    }
                    R.id.menu_add_image -> {
                        if (imageUris.size >= 4) return true
                        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    }
                    R.id.menu_visibility_public, R.id.menu_visibility_unlisted,
                    R.id.menu_visibility_private, R.id.menu_visibility_direct -> {
                        val anim = AnimationUtils.loadAnimation(
                            activity,
                            R.anim.anim_toolbar_optinal_view_open
                        )
                        if (binding.viewVisibility.visibility == View.GONE) {
                            binding.viewVisibility.visibility = View.VISIBLE
                            binding.viewVisibility.startAnimation(anim)
                        } else {
                            binding.viewVisibility.visibility = View.GONE
                        }
                    }
                    R.id.menu_nsfw_to_on -> {
                        viewModel.setSensitive(true)
                    }
                    R.id.menu_nsfw_to_off -> {
                        viewModel.setSensitive(false)
                    }
                    R.id.menu_content_warning -> {
                        if (binding.inputSpoiler.visibility == View.VISIBLE) {
                            binding.inputSpoiler.setText("")
                            viewModel.setSpoilerTextVisibility(false)
                        } else {
                            viewModel.setSpoilerTextVisibility(true)
                        }
                    }
                }
                return true
            }
        }, viewLifecycleOwner)
    }

    override fun onDismiss(dialog: DialogInterface) {
        // finish parent activity when attached this fragment
        if (isAdded) {
            requireActivity().finish()
        }
        super.onDismiss(dialog)
    }
}