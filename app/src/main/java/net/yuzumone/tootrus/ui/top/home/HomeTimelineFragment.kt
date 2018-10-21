package net.yuzumone.tootrus.ui.top.home

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
import android.widget.Toast
import com.sys1yagi.mastodon4j.api.entity.Status
import dagger.android.support.AndroidSupportInjection
import net.yuzumone.tootrus.R
import net.yuzumone.tootrus.databinding.FragmentHomeTimelineBinding
import net.yuzumone.tootrus.ui.PostStatusActivity
import net.yuzumone.tootrus.ui.StatusDetailActivity
import net.yuzumone.tootrus.ui.common.StatusBindingAdapter
import net.yuzumone.tootrus.ui.top.TopViewModel
import javax.inject.Inject

class HomeTimelineFragment : Fragment() {

    private lateinit var binding: FragmentHomeTimelineBinding
    private lateinit var topViewModel: TopViewModel
    private lateinit var adapter: StatusBindingAdapter
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
        binding = FragmentHomeTimelineBinding.inflate(inflater, container, false).apply {
            recyclerTimeline.adapter = adapter
            recyclerTimeline.layoutManager = layoutManager
            recyclerTimeline.addItemDecoration(divider)
            recyclerTimeline.itemAnimator = null
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        topViewModel.homeStatuses.observe(this, Observer {
            if (binding.progress.visibility == View.VISIBLE) {
                binding.progress.visibility = View.GONE
            }
            adapter.update(it)
        })
        topViewModel.homeError.observe(this, Observer {
            if (binding.progress.visibility == View.VISIBLE) {
                binding.progress.visibility = View.GONE
            }
            Toast.makeText(activity, R.string.error, Toast.LENGTH_SHORT).show()
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