package net.yuzumone.tootrus.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import net.yuzumone.tootrus.R
import net.yuzumone.tootrus.databinding.FragmentProfileStatusesBinding
import net.yuzumone.tootrus.ui.common.StatusBindingAdapter

@AndroidEntryPoint
class ProfileStatusesFragment : Fragment() {

    private val profileViewModel: ProfileViewModel by activityViewModels()
    private lateinit var binding: FragmentProfileStatusesBinding
    private lateinit var adapter: StatusBindingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        adapter = StatusBindingAdapter(profileViewModel)
        val layoutManager = LinearLayoutManager(activity)
        val divider = DividerItemDecoration(activity, layoutManager.orientation)
        divider.setDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.divider)!!)
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
        profileViewModel.statuses.observe(viewLifecycleOwner) {
            adapter.update(it)
        }
    }
}
