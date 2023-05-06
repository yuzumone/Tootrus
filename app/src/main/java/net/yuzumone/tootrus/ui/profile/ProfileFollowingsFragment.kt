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
import net.yuzumone.tootrus.databinding.FragmentProfileFollowingsBinding
import net.yuzumone.tootrus.ui.common.AccountBindingAdapter
import javax.inject.Inject

class ProfileFollowingsFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var binding: FragmentProfileFollowingsBinding
    private lateinit var adapter: AccountBindingAdapter
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
        profileViewModel.followings.observe(viewLifecycleOwner, Observer {
            adapter.update(it)
        })
    }
}
