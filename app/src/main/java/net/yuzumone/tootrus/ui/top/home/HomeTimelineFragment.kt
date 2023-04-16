package net.yuzumone.tootrus.ui.top.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.android.support.AndroidSupportInjection
import net.yuzumone.tootrus.R
import net.yuzumone.tootrus.databinding.FragmentHomeTimelineBinding
import net.yuzumone.tootrus.ui.common.StatusBindingAdapter
import net.yuzumone.tootrus.ui.top.TopViewModel
import javax.inject.Inject

class HomeTimelineFragment : Fragment() {

    private lateinit var binding: FragmentHomeTimelineBinding
    private lateinit var topViewModel: TopViewModel
    private lateinit var adapter: StatusBindingAdapter
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        topViewModel = ViewModelProviders.of(requireActivity(), viewModelFactory)
                .get(TopViewModel::class.java)
        adapter = StatusBindingAdapter(topViewModel, topViewModel)
        val layoutManager = LinearLayoutManager(activity)
        val divider = DividerItemDecoration(activity, layoutManager.orientation)
        divider.setDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.divider)!!)
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
        topViewModel.homeStatuses.observe(viewLifecycleOwner, Observer {
            if (binding.progress.visibility == View.VISIBLE) {
                binding.progress.visibility = View.GONE
            }
            adapter.update(it)
        })
        topViewModel.homeError.observe(viewLifecycleOwner, Observer {
            if (binding.progress.visibility == View.VISIBLE) {
                binding.progress.visibility = View.GONE
            }
            Toast.makeText(activity, R.string.error, Toast.LENGTH_SHORT).show()
        })
    }
}