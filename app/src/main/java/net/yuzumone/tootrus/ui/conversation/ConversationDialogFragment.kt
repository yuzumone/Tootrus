package net.yuzumone.tootrus.ui.conversation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.sys1yagi.mastodon4j.api.entity.Status
import dagger.android.support.AndroidSupportInjection
import net.yuzumone.tootrus.R
import net.yuzumone.tootrus.databinding.FragmentConversionBinding
import net.yuzumone.tootrus.ui.StatusDetailActivity
import net.yuzumone.tootrus.ui.common.StatusBindingAdapter
import javax.inject.Inject

class ConversationDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentConversionBinding
    private lateinit var viewModel: ConversationViewModel
    private lateinit var adapter: StatusBindingAdapter
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private val status by lazy {
        Gson().fromJson(arguments?.getString(ARG_STATUS), Status::class.java)
    }

    companion object {
        private const val ARG_STATUS = "status"
        fun newReplyInstance(status: Status): ConversationDialogFragment {
            return ConversationDialogFragment().apply {
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
        viewModel = ViewModelProvider(this, viewModelFactory)[ConversationViewModel::class.java]
        adapter = StatusBindingAdapter(viewModel)
        val layoutManager = LinearLayoutManager(activity)
        val divider = DividerItemDecoration(activity, layoutManager.orientation)
        divider.setDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.divider)!!)
        binding = FragmentConversionBinding.inflate(inflater, container, false).also {
            it.viewModel = viewModel
            it.recyclerConversation.adapter = adapter
            it.recyclerConversation.layoutManager = layoutManager
            it.recyclerConversation.addItemDecoration(divider)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.also {
            it.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
            it.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
        viewModel.getConversations(status)
        viewModel.conversations.observe(viewLifecycleOwner, Observer {
            binding.progress.visibility = View.GONE
            adapter.update(it)
        })
        viewModel.error.observe(viewLifecycleOwner, Observer {
            binding.progress.visibility = View.GONE
            Toast.makeText(activity, R.string.error, Toast.LENGTH_SHORT).show()
        })
        viewModel.eventNavigationClick.observe(viewLifecycleOwner, Observer {
            dismiss()
        })
        viewModel.eventOpenStatus.observe(viewLifecycleOwner, Observer {
            requireActivity().run {
                val intent = StatusDetailActivity.createIntent(this, it.id)
                startActivity(intent)
            }
        })
    }
}