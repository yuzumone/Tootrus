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
import net.yuzumone.tootrus.databinding.FragmentProfileMediaStatusesBinding
import net.yuzumone.tootrus.ui.common.StatusBindingAdapter

@AndroidEntryPoint
class ProfileMediaStatusesFragment : Fragment() {

    private val profileViewModel: ProfileViewModel by activityViewModels()
    private lateinit var binding: FragmentProfileMediaStatusesBinding
    private lateinit var adapter: StatusBindingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        adapter = StatusBindingAdapter(profileViewModel)
        val layoutManager = LinearLayoutManager(activity)
        val divider = DividerItemDecoration(activity, layoutManager.orientation)
        divider.setDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.divider)!!)
        binding = FragmentProfileMediaStatusesBinding.inflate(inflater, container, false).also {
            it.recyclerProfileMediaStatuses.adapter = adapter
            it.recyclerProfileMediaStatuses.layoutManager = layoutManager
            it.recyclerProfileMediaStatuses.addItemDecoration(divider)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileViewModel.mediaStatuses.observe(viewLifecycleOwner) {
            adapter.update(it)
        }
    }
}
