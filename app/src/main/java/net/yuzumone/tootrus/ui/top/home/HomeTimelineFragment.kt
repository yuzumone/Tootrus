package net.yuzumone.tootrus.ui.top.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import net.yuzumone.tootrus.R
import net.yuzumone.tootrus.databinding.FragmentHomeTimelineBinding
import net.yuzumone.tootrus.ui.common.StatusBindingAdapter
import net.yuzumone.tootrus.ui.top.TopViewModel

@AndroidEntryPoint
class HomeTimelineFragment : Fragment() {

    private lateinit var binding: FragmentHomeTimelineBinding
    private val topViewModel: TopViewModel by activityViewModels()
    private lateinit var adapter: StatusBindingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
        topViewModel.homeStatuses.observe(viewLifecycleOwner) {
            if (binding.progress.visibility == View.VISIBLE) {
                binding.progress.visibility = View.GONE
            }
            adapter.update(it)
        }
        topViewModel.homeError.observe(viewLifecycleOwner) {
            if (binding.progress.visibility == View.VISIBLE) {
                binding.progress.visibility = View.GONE
            }
            Toast.makeText(activity, R.string.error, Toast.LENGTH_SHORT).show()
        }
    }
}
