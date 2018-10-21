package net.yuzumone.tootrus.ui.top.local

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
import net.yuzumone.tootrus.databinding.FragmentLocalTimelineBinding
import net.yuzumone.tootrus.ui.PostStatusActivity
import net.yuzumone.tootrus.ui.StatusDetailActivity
import net.yuzumone.tootrus.ui.common.StatusBindingAdapter
import net.yuzumone.tootrus.ui.top.TopViewModel
import javax.inject.Inject

class LocalTimelineFragment : Fragment() {

    private lateinit var binding: FragmentLocalTimelineBinding
    private lateinit var adapter: StatusBindingAdapter
    private lateinit var topViewModel: TopViewModel
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        topViewModel = ViewModelProviders.of(activity!!, viewModelFactory)
                .get(TopViewModel::class.java)
        adapter = StatusBindingAdapter(handleDetail(), handleReply(), handleFavorite(), handleReblog())
        val layoutManager = LinearLayoutManager(activity)
        val divider = DividerItemDecoration(activity, layoutManager.orientation)
        divider.setDrawable(ContextCompat.getDrawable(activity!!, R.drawable.divider)!!)
        binding = FragmentLocalTimelineBinding.inflate(inflater, container, false).apply {
            recyclerLocalTimeline.adapter = adapter
            recyclerLocalTimeline.layoutManager = layoutManager
            recyclerLocalTimeline.addItemDecoration(divider)
            recyclerLocalTimeline.itemAnimator = null
            swipeRefresh.setColorSchemeResources(R.color.colorPrimary)
            swipeRefresh.setOnRefreshListener { topViewModel.updateLocalTimeline() }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        topViewModel.localStatuses.observe(this, Observer {
            binding.swipeRefresh.isRefreshing = false
            adapter.update(it)
        })
        topViewModel.localError.observe(this, Observer {
            if (binding.swipeRefresh.isRefreshing) {
                binding.swipeRefresh.isRefreshing = false
            }
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
        if (it.isFavourited) {
            topViewModel.postUnfavorite(it)
        } else {
            topViewModel.postFavorite(it)
        }
    }

    private fun handleReblog(): (Status) -> Unit = {
        if (it.isReblogged) {
            topViewModel.postReblog(it)
        }
    }
}