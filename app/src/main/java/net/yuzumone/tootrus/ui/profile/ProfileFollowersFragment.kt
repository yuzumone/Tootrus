package net.yuzumone.tootrus.ui.profile

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
import dagger.android.support.AndroidSupportInjection
import net.yuzumone.tootrus.R
import net.yuzumone.tootrus.databinding.FragmentProfileFollowersBinding
import net.yuzumone.tootrus.ui.common.AccountBindingAdapter
import javax.inject.Inject

class ProfileFollowersFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var binding: FragmentProfileFollowersBinding
    private lateinit var adapter: AccountBindingAdapter
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        profileViewModel = ViewModelProviders.of(activity!!, viewModelFactory)
                .get(ProfileViewModel::class.java)
        adapter = AccountBindingAdapter()
        val layoutManager = LinearLayoutManager(activity)
        val divider = DividerItemDecoration(activity, layoutManager.orientation)
        divider.setDrawable(ContextCompat.getDrawable(activity!!, R.drawable.divider)!!)
        binding = FragmentProfileFollowersBinding.inflate(inflater, container, false).apply {
            recyclerProfileFollowers.adapter = adapter
            recyclerProfileFollowers.layoutManager = layoutManager
            recyclerProfileFollowers.addItemDecoration(divider)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileViewModel.followers.observe(this, Observer {
            adapter.update(it)
        })
    }
}