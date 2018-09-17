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
import dagger.android.support.AndroidSupportInjection
import net.yuzumone.tootrus.R
import net.yuzumone.tootrus.databinding.FragmentLocalTimelineBinding
import net.yuzumone.tootrus.ui.common.StatusBindingAdapter
import javax.inject.Inject

class LocalTimelineFragment : Fragment() {

    private lateinit var binding: FragmentLocalTimelineBinding
    private lateinit var adapter: StatusBindingAdapter
    private lateinit var localTimelineViewModel: LocalTimelineViewModel
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        localTimelineViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(LocalTimelineViewModel::class.java)
        adapter = StatusBindingAdapter()
        val layoutManager = LinearLayoutManager(activity)
        val divider = DividerItemDecoration(activity, layoutManager.orientation)
        divider.setDrawable(ContextCompat.getDrawable(activity!!, R.drawable.divider)!!)
        binding = FragmentLocalTimelineBinding.inflate(inflater, container, false).apply {
            recyclerLocalTimeline.adapter = adapter
            recyclerLocalTimeline.layoutManager = layoutManager
            recyclerLocalTimeline.addItemDecoration(divider)
            swipeRefresh.setColorSchemeResources(R.color.colorPrimary)
            swipeRefresh.setOnRefreshListener { localTimelineViewModel.updateLocalTimeline() }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        localTimelineViewModel.statuses.observe(this, Observer {
            binding.swipeRefresh.isRefreshing = false
            adapter.update(it)
        })
        localTimelineViewModel.error.observe(this, Observer {
            binding.swipeRefresh.isRefreshing = false
        })
    }
}