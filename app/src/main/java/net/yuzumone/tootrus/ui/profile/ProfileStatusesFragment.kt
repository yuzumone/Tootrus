package net.yuzumone.tootrus.ui.profile

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sys1yagi.mastodon4j.api.entity.Status
import dagger.android.support.AndroidSupportInjection
import net.yuzumone.tootrus.R
import net.yuzumone.tootrus.databinding.FragmentProfileStatusesBinding
import net.yuzumone.tootrus.ui.PostStatusActivity
import net.yuzumone.tootrus.ui.ProfileActivity
import net.yuzumone.tootrus.ui.StatusDetailActivity
import net.yuzumone.tootrus.ui.common.StatusBindingAdapter
import javax.inject.Inject

class ProfileStatusesFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var binding: FragmentProfileStatusesBinding
    private lateinit var adapter: StatusBindingAdapter
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        profileViewModel = ViewModelProviders.of(activity!!, viewModelFactory)
                .get(ProfileViewModel::class.java)
        adapter = StatusBindingAdapter(handleDetail(), handleReply(), handleFavorite(), handleReblog(), handleMenu())
        val layoutManager = LinearLayoutManager(activity)
        val divider = DividerItemDecoration(activity, layoutManager.orientation)
        divider.setDrawable(ContextCompat.getDrawable(activity!!, R.drawable.divider)!!)
        binding = FragmentProfileStatusesBinding.inflate(inflater, container, false).apply {
            recyclerProfileStatuses.adapter = adapter
            recyclerProfileStatuses.layoutManager = layoutManager
            recyclerProfileStatuses.addItemDecoration(divider)
            recyclerProfileStatuses.itemAnimator = null
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileViewModel.statuses.observe(this, Observer {
            adapter.update(it)
        })
    }

    private fun handleReply(): (Status) -> Unit = { status ->
        requireActivity().run {
            val intent = PostStatusActivity.createReplyIntent(this, status.account!!.acct, status.id)
            startActivity(intent)
        }
    }

    private fun handleDetail(): (Status) -> Unit = {
        requireActivity().run {
            val intent = StatusDetailActivity.createIntent(this, it.id)
            startActivity(intent)
        }
    }

    private fun handleFavorite(): (Status) -> Unit = {

    }

    private fun handleReblog(): (Status) -> Unit = {

    }

    private fun handleMenu(): (Status) -> Unit = {
        requireActivity().run {
            val intent = ProfileActivity.createIntent(this, it.account!!.id)
            startActivity(intent)
        }
    }
}