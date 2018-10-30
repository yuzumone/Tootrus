package net.yuzumone.tootrus.ui.profile

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import dagger.android.support.AndroidSupportInjection
import net.yuzumone.tootrus.R
import net.yuzumone.tootrus.databinding.FragmentProfileBinding
import javax.inject.Inject

class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var binding: FragmentProfileBinding
    private val id by lazy { arguments!!.getLong(ARG_ID) }
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    companion object {
        private const val ARG_ID = "id"
        fun newInstance(id: Long): ProfileFragment {
            return ProfileFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_ID, id)
                }
            }
        }
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        profileViewModel = ViewModelProviders.of(activity!!, viewModelFactory)
                .get(ProfileViewModel::class.java)
        val adapter = ViewPagerAdapter(childFragmentManager).apply {
            add("Toots", ProfileStatusesFragment())
            add("Follows", ProfileFollowingsFragment())
            add("Followers", ProfileFollowersFragment())
        }
        binding = FragmentProfileBinding.inflate(inflater, container, false).apply {
            pager.adapter = adapter
            pager.offscreenPageLimit = 2
            viewModel = profileViewModel
        }
        binding.tab.setupWithViewPager(binding.pager)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileViewModel.getAccountAndRelationShip(id)
        profileViewModel.getStatuses(id)
        profileViewModel.getFollowing(id)
        profileViewModel.getFollowers(id)
        profileViewModel.account.observe(this, Observer {
            binding.account = it
        })
        profileViewModel.relationship.observe(this, Observer {
            binding.relationship = it
        })
        profileViewModel.error.observe(this, Observer {
            Toast.makeText(activity, R.string.error, Toast.LENGTH_SHORT).show()
        })
    }

    class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        private val fragments = ArrayList<Fragment>()
        private val titles = ArrayList<String>()

        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getCount(): Int {
            return fragments.size
        }

        override fun getPageTitle(position: Int): CharSequence {
            return titles[position]
        }

        fun add(title: String, fragment: Fragment) {
            fragments.add(fragment)
            titles.add(title)
            notifyDataSetChanged()
        }
    }
}