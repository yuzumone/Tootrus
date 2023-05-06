package net.yuzumone.tootrus.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.android.support.AndroidSupportInjection
import net.yuzumone.tootrus.R
import net.yuzumone.tootrus.databinding.FragmentProfileMediaStatusesBinding
import net.yuzumone.tootrus.ui.common.StatusBindingAdapter
import javax.inject.Inject

class ProfileMediaStatusesFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var binding: FragmentProfileMediaStatusesBinding
    private lateinit var adapter: StatusBindingAdapter
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        profileViewModel =
            ViewModelProvider(requireActivity(), viewModelFactory)[ProfileViewModel::class.java]
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
        profileViewModel.mediaStatuses.observe(viewLifecycleOwner, Observer {
            adapter.update(it)
        })
    }
}
