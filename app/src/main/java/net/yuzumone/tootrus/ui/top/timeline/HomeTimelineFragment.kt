package net.yuzumone.tootrus.ui.top.timeline

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
import dagger.android.support.AndroidSupportInjection
import net.yuzumone.tootrus.R
import net.yuzumone.tootrus.databinding.FragmentTimelineBinding
import net.yuzumone.tootrus.ui.common.StatusBindingAdapter
import net.yuzumone.tootrus.ui.top.TopViewModel
import net.yuzumone.tootrus.vo.TootrusStatus
import javax.inject.Inject

class HomeTimelineFragment : Fragment() {

    private lateinit var binding: FragmentTimelineBinding
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
        adapter = StatusBindingAdapter(handleFavorite())
        val layoutManager = LinearLayoutManager(activity)
        val divider = DividerItemDecoration(activity, layoutManager.orientation)
        divider.setDrawable(ContextCompat.getDrawable(activity!!, R.drawable.divider)!!)
        binding = FragmentTimelineBinding.inflate(inflater, container, false).apply {
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
            adapter.update(it)
        })
        topViewModel.favoritedStatus.observe(this, Observer {
            adapter.notifyDataSetChanged()
            Toast.makeText(activity, getString(R.string.favorited), Toast.LENGTH_SHORT).show()
        })
        topViewModel.unfavoriteStatus.observe(this, Observer {
            adapter.notifyDataSetChanged()
            Toast.makeText(activity, getString(R.string.unfavorite), Toast.LENGTH_SHORT).show()
        })
        topViewModel.error.observe(this, Observer {
            Toast.makeText(activity, R.string.error, Toast.LENGTH_SHORT).show()
        })
    }

    private fun handleFavorite(): (TootrusStatus) -> Unit = {
        if (it.isFavorited) {
            topViewModel.postUnfavorite(it)
        } else {
            topViewModel.postFavorite(it)
        }
    }
}