package net.yuzumone.tootrus.ui.poststatus

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.gson.Gson
import com.sys1yagi.mastodon4j.api.entity.Status
import net.yuzumone.tootrus.R
import net.yuzumone.tootrus.databinding.FragmentPostStatusBinding
import net.yuzumone.tootrus.service.PostStatusService
import net.yuzumone.tootrus.ui.common.PostImageView
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

class PostStatusDialogFragment : DialogFragment() {

    private val imageUris = ArrayList<String>()
    private lateinit var binding: FragmentPostStatusBinding
    private lateinit var viewModel: PostStatusDialogViewModel
    private val repliedStatus: Status? by lazy {
        Gson().fromJson(arguments?.getString(ARG_REPLIED_STATUS), Status::class.java)
    }

    companion object {
        private const val ARG_REPLIED_STATUS = "replied_status"
        private const val RC_READ_EXTERNAL_STORAGE = 1
        private const val PICK_FROM_GALLERY = 10
        fun newReplyInstance(status: Status): PostStatusDialogFragment {
            return PostStatusDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_REPLIED_STATUS, Gson().toJson(status))
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProviders.of(this).get(PostStatusDialogViewModel::class.java)
        viewModel.setRepliedStatus(repliedStatus)
        binding = FragmentPostStatusBinding.inflate(inflater, container, false).also {
            it.viewModel = viewModel
            it.lifecycleOwner = viewLifecycleOwner
            it.viewPostImage.setOnImageItemClickListener(object : PostImageView.OnImageItemClickListener {
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
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog.window?.also {
            it.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            it.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.statusVisibility.observe(viewLifecycleOwner, Observer {
            updateVisibilityMenu(it)
            if (binding.viewVisibility.visibility == View.VISIBLE) {
                binding.viewVisibility.visibility = View.GONE
            }
        })
        viewModel.isSensitive.observe(viewLifecycleOwner, Observer {
            updateNsfwMenu(it)
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
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.let {
            it.inflate(R.menu.menu_post_status, menu)
            it.inflate(R.menu.menu_visibility_public, menu)
            it.inflate(R.menu.menu_nsfw_to_on, menu)
            it.inflate(R.menu.menu_content_warning, menu)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_post_status -> {
                viewModel.postStatus()
                dismiss()
            }
            R.id.menu_add_image -> {
                if (imageUris.size < 4) {
                    selectImage()
                }
            }
            R.id.menu_visibility_public, R.id.menu_visibility_unlisted,
            R.id.menu_visibility_private, R.id.menu_visibility_direct -> {
                val anim = AnimationUtils.loadAnimation(activity, R.anim.anim_toolbar_optinal_view_open)
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

    private fun updateVisibilityMenu(visibility: Status.Visibility) {
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

    private fun updateNsfwMenu(isSensitive: Boolean) {
        binding.toolbar.menu.removeItem(R.id.menu_nsfw_to_on)
        binding.toolbar.menu.removeItem(R.id.menu_nsfw_to_off)
        if (isSensitive) {
            binding.toolbar.inflateMenu(R.menu.menu_nsfw_to_off)
        } else {
            binding.toolbar.inflateMenu(R.menu.menu_nsfw_to_on)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    @AfterPermissionGranted(RC_READ_EXTERNAL_STORAGE)
    private fun selectImage() {
        if (EasyPermissions.hasPermissions(activity!!, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            startActivityForResult(Intent.createChooser(intent, "Choose a photo"), PICK_FROM_GALLERY)
        } else {
            EasyPermissions.requestPermissions(this, "Request read external storage",
                    RC_READ_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            data ?: return
            data.data?.let {
                imageUris.add(it.toString())
                viewModel.setImageUris(imageUris)
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface?) {
        // finish parent activity when attached this fragment
        if (isAdded) {
            requireActivity().finish()
        }
        super.onDismiss(dialog)
    }
}