package net.yuzumone.tootrus.ui.profile

import android.os.Bundle
import android.util.Log
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
import net.yuzumone.tootrus.databinding.FragmentProfileFollowersBinding
import net.yuzumone.tootrus.ui.common.AccountBindingAdapter

@AndroidEntryPoint
class ProfileFollowersFragment : Fragment() {

    private val profileViewModel: ProfileViewModel by activityViewModels()
    private lateinit var binding: FragmentProfileFollowersBinding
    private lateinit var adapter: AccountBindingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        adapter = AccountBindingAdapter(profileViewModel)
        val layoutManager = LinearLayoutManager(activity)
        val divider = DividerItemDecoration(activity, layoutManager.orientation)
        divider.setDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.divider)!!)
        binding = FragmentProfileFollowersBinding.inflate(inflater, container, false).apply {
            recyclerProfileFollowers.adapter = adapter
            recyclerProfileFollowers.layoutManager = layoutManager
            recyclerProfileFollowers.addItemDecoration(divider)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("ProfileFollowers", profileViewModel.toString())
        profileViewModel.followers.observe(viewLifecycleOwner) {
            adapter.update(it)
        }
    }
}
