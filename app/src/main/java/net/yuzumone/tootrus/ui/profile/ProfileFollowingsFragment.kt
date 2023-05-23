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
import net.yuzumone.tootrus.databinding.FragmentProfileFollowingsBinding
import net.yuzumone.tootrus.ui.common.AccountBindingAdapter

@AndroidEntryPoint
class ProfileFollowingsFragment : Fragment() {

    private val profileViewModel: ProfileViewModel by activityViewModels()
    private lateinit var binding: FragmentProfileFollowingsBinding
    private lateinit var adapter: AccountBindingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        adapter = AccountBindingAdapter(profileViewModel)
        val layoutManager = LinearLayoutManager(activity)
        val divider = DividerItemDecoration(activity, layoutManager.orientation)
        divider.setDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.divider)!!)
        binding = FragmentProfileFollowingsBinding.inflate(inflater, container, false).apply {
            recyclerProfileFollowings.adapter = adapter
            recyclerProfileFollowings.layoutManager = layoutManager
            recyclerProfileFollowings.addItemDecoration(divider)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("ProfileFollowings", profileViewModel.toString())
        profileViewModel.followings.observe(viewLifecycleOwner) {
            adapter.update(it)
        }
    }
}
