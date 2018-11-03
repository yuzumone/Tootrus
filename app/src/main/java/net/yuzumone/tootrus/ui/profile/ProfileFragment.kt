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
import net.yuzumone.tootrus.ui.PostStatusActivity
import net.yuzumone.tootrus.ui.StatusDetailActivity
import net.yuzumone.tootrus.ui.menu.MenuDialogFragment
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
            toolbar.setNavigationIcon(R.drawable.ic_action_back)
            toolbar.setNavigationOnClickListener {
                activity?.finish()
            }
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
        profileViewModel.userId.observe(this, Observer {
            binding.userId = it
        })
        profileViewModel.account.observe(this, Observer {
            binding.account = it
            binding.toolbar.title = it?.userName
        })
        profileViewModel.relationship.observe(this, Observer {
            binding.relationship = it
        })
        profileViewModel.detailActionEvent.observe(this, Observer {
            it ?: return@Observer
            requireActivity().run {
                val intent = StatusDetailActivity.createIntent(this, it.id)
                startActivity(intent)
            }
        })
        profileViewModel.replyActionEvent.observe(this, Observer {
            it ?: return@Observer
            requireActivity().run {
                val intent = PostStatusActivity
                        .createReplyIntent(this, it.account!!.acct, it.id)
                startActivity(intent)
            }
        })
        profileViewModel.favoriteActionEvent.observe(this, Observer {
            Toast.makeText(activity, getString(R.string.favorited), Toast.LENGTH_SHORT).show()
        })
        profileViewModel.unfavoriteActionEvent.observe(this, Observer {
            Toast.makeText(activity, getString(R.string.unfavorite), Toast.LENGTH_SHORT).show()
        })
        profileViewModel.reblogActionEvent.observe(this, Observer {
            Toast.makeText(activity, getString(R.string.reblogged), Toast.LENGTH_SHORT).show()
        })
        profileViewModel.menuActionEvent.observe(this, Observer {
            it ?: return@Observer
            val fragment = MenuDialogFragment.newInstance(it)
            fragment.show(fragmentManager, "menu")
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