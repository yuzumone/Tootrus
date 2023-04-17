package net.yuzumone.tootrus.ui.detail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.sys1yagi.mastodon4j.api.entity.Status
import dagger.android.support.AndroidSupportInjection
import net.yuzumone.tootrus.R
import net.yuzumone.tootrus.databinding.FragmentStatusDetailBinding
import net.yuzumone.tootrus.ui.PostStatusActivity
import net.yuzumone.tootrus.ui.menu.MenuDialogFragment
import javax.inject.Inject

class StatusDetailFragment : Fragment() {

    private lateinit var binding: FragmentStatusDetailBinding
    private lateinit var statusDetailViewModel: StatusDetailViewModel
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private val status by lazy {
        Gson().fromJson(requireArguments().getString(ARG_STATUS), Status::class.java)
    }

    companion object {
        private const val ARG_STATUS = "status"
        fun newInstance(status: Status): StatusDetailFragment {
            return StatusDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_STATUS, Gson().toJson(status))
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        statusDetailViewModel = ViewModelProvider(this, viewModelFactory)[StatusDetailViewModel::class.java]
        binding = FragmentStatusDetailBinding.inflate(inflater, container, false).also {
            it.listener = statusDetailViewModel
            it.status = if (status.reblog == null) status else status.reblog
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        statusDetailViewModel.replyActionEvent.observe(viewLifecycleOwner, Observer {
            it ?: return@Observer
            requireActivity().run {
                val intent = PostStatusActivity.createReplyIntent(this, it)
                startActivity(intent)
            }
        })
        statusDetailViewModel.favoriteActionEvent.observe(viewLifecycleOwner, Observer {
            binding.status = it
            Toast.makeText(activity, getString(R.string.favorited), Toast.LENGTH_SHORT).show()
        })
        statusDetailViewModel.unfavoriteActionEvent.observe(viewLifecycleOwner, Observer {
            binding.status = it
            Toast.makeText(activity, getString(R.string.unfavorite), Toast.LENGTH_SHORT).show()
        })
        statusDetailViewModel.reblogActionEvent.observe(viewLifecycleOwner, Observer {
            binding.status = it
            Toast.makeText(activity, getString(R.string.reblogged), Toast.LENGTH_SHORT).show()
        })
        statusDetailViewModel.openMenuActionEvent.observe(viewLifecycleOwner, Observer {
            it ?: return@Observer
            val fragment = MenuDialogFragment.newInstance(it)
            fragment.show(parentFragmentManager, "menu")
        })
        statusDetailViewModel.favoriteError.observe(viewLifecycleOwner, Observer {
            Toast.makeText(activity, R.string.error, Toast.LENGTH_SHORT).show()
        })
        statusDetailViewModel.reblogError.observe(viewLifecycleOwner, Observer {
            Toast.makeText(activity, R.string.error, Toast.LENGTH_SHORT).show()
        })
    }
}