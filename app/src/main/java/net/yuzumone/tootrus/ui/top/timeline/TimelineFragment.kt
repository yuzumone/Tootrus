package net.yuzumone.tootrus.ui.top.timeline

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.AndroidSupportInjection
import net.yuzumone.tootrus.databinding.FragmentTimelineBinding
import javax.inject.Inject

class TimelineFragment : Fragment() {

    private lateinit var binding: FragmentTimelineBinding
    private lateinit var timelineViewModel: TimelineViewModel
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        timelineViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(TimelineViewModel::class.java)
        binding = FragmentTimelineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        timelineViewModel.statuses.observe(this, Observer {
            it?.forEach { status ->
                Log.d("Timeline", status.content)
            }
        })
        timelineViewModel.error.observe(this, Observer {
            Log.d("Timeline", it.toString())
        })
    }
}