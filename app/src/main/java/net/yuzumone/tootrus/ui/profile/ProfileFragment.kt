package net.yuzumone.tootrus.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import com.sys1yagi.mastodon4j.api.entity.Account
import com.sys1yagi.mastodon4j.api.entity.Relationship
import dagger.android.support.AndroidSupportInjection
import net.yuzumone.tootrus.R
import net.yuzumone.tootrus.databinding.FragmentProfileBinding
import net.yuzumone.tootrus.ui.PostStatusActivity
import net.yuzumone.tootrus.ui.ProfileActivity
import net.yuzumone.tootrus.ui.StatusDetailActivity
import net.yuzumone.tootrus.ui.menu.MenuDialogFragment
import javax.inject.Inject

class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var binding: FragmentProfileBinding
    private val account by lazy {
        Gson().fromJson(requireArguments().getString(ARG_ACCOUNT), Account::class.java)
    }
    private val relationship by lazy {
        Gson().fromJson(requireArguments().getString(ARG_RELATIONSHIP), Relationship::class.java)
    }
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    companion object {
        private const val ARG_ACCOUNT = "account"
        private const val ARG_RELATIONSHIP = "relationship"
        fun newInstance(account: Account, relationship: Relationship): ProfileFragment {
            return ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_ACCOUNT, Gson().toJson(account))
                    putString(ARG_RELATIONSHIP, Gson().toJson(relationship))
                }
            }
        }
    }

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
        val adapter = ViewPagerAdapter(requireActivity()).apply {
            add(getString(R.string.profile_toot, account.statusesCount), ProfileStatusesFragment())
            add(getString(R.string.profile_media), ProfileMediaStatusesFragment())
            add(
                getString(R.string.profile_follows, account.followingCount),
                ProfileFollowingsFragment()
            )
            add(
                getString(R.string.profile_followers, account.followersCount),
                ProfileFollowersFragment()
            )
        }
        binding = FragmentProfileBinding.inflate(inflater, container, false).also {
            it.account = account
            it.relationship = relationship
            it.pager.adapter = adapter
            it.pager.offscreenPageLimit = 2
            it.toolbar.setNavigationIcon(R.drawable.ic_action_back)
            it.toolbar.setNavigationOnClickListener { _ ->
                activity?.finish()
            }
            it.viewModel = profileViewModel
        }
        TabLayoutMediator(binding.tab, binding.pager) { tab, position ->
            tab.text = adapter.getPageTitle(position)
        }.attach()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileViewModel.getStatusesWithPinned(account.id)
        profileViewModel.getMediaStatuses(account.id)
        profileViewModel.getFollowing(account.id)
        profileViewModel.getFollowers(account.id)
        profileViewModel.userId.observe(viewLifecycleOwner, Observer {
            binding.userId = it
        })
        profileViewModel.account.observe(viewLifecycleOwner, Observer {
            binding.account = it
            binding.toolbar.title = it?.userName
        })
        profileViewModel.relationship.observe(viewLifecycleOwner, Observer {
            binding.relationship = it
        })
        profileViewModel.detailActionEvent.observe(viewLifecycleOwner, Observer {
            it ?: return@Observer
            requireActivity().run {
                val intent = StatusDetailActivity.createIntent(this, it.id)
                startActivity(intent)
            }
        })
        profileViewModel.replyActionEvent.observe(viewLifecycleOwner, Observer {
            it ?: return@Observer
            requireActivity().run {
                val intent = PostStatusActivity.createReplyIntent(this, it)
                startActivity(intent)
            }
        })
        profileViewModel.favoriteActionEvent.observe(viewLifecycleOwner, Observer {
            Toast.makeText(activity, getString(R.string.favorited), Toast.LENGTH_SHORT).show()
        })
        profileViewModel.unfavoriteActionEvent.observe(viewLifecycleOwner, Observer {
            Toast.makeText(activity, getString(R.string.unfavorite), Toast.LENGTH_SHORT).show()
        })
        profileViewModel.reblogActionEvent.observe(viewLifecycleOwner, Observer {
            Toast.makeText(activity, getString(R.string.reblogged), Toast.LENGTH_SHORT).show()
        })
        profileViewModel.menuActionEvent.observe(viewLifecycleOwner, Observer {
            it ?: return@Observer
            val fragment = MenuDialogFragment.newInstance(it)
            fragment.show(parentFragmentManager, "menu")
        })
        profileViewModel.openAccount.observe(viewLifecycleOwner, Observer {
            requireActivity().run {
                val intent = ProfileActivity.createIntent(this, it.id)
                startActivity(intent)
            }
        })
        profileViewModel.error.observe(viewLifecycleOwner, Observer {
            Toast.makeText(activity, R.string.error, Toast.LENGTH_SHORT).show()
        })
    }

    class ViewPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

        private val fragments = ArrayList<Fragment>()
        private val titles = ArrayList<String>()

        override fun getItemCount(): Int = fragments.size

        override fun createFragment(position: Int): Fragment = fragments[position]

        fun getPageTitle(position: Int): String = titles[position]

        fun add(title: String, fragment: Fragment) {
            fragments.add(fragment)
            titles.add(title)
        }
    }
}
