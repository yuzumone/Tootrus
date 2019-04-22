package net.yuzumone.tootrus.ui.top

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.navigation.NavigationView
import dagger.android.support.AndroidSupportInjection
import net.yuzumone.tootrus.R
import net.yuzumone.tootrus.databinding.FragmentTopBinding
import net.yuzumone.tootrus.ui.PostStatusActivity
import net.yuzumone.tootrus.ui.ProfileActivity
import net.yuzumone.tootrus.ui.StatusDetailActivity
import net.yuzumone.tootrus.ui.menu.MenuDialogFragment
import net.yuzumone.tootrus.ui.top.home.HomeTimelineFragment
import net.yuzumone.tootrus.ui.top.local.LocalTimelineFragment
import net.yuzumone.tootrus.ui.top.notification.NotificationFragment
import net.yuzumone.tootrus.util.addOnPageChangeListener
import javax.inject.Inject

class TopFragment : Fragment(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: FragmentTopBinding
    private lateinit var topViewModel: TopViewModel
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        topViewModel = ViewModelProviders.of(activity!!, viewModelFactory)
                .get(TopViewModel::class.java)
        topViewModel.startUserStream()
        val adapter = ViewPagerAdapter(childFragmentManager).apply {
            add(getString(R.string.section_home), HomeTimelineFragment())
            add(getString(R.string.section_notification), NotificationFragment())
            add(getString(R.string.section_local_timeline), LocalTimelineFragment())
        }
        binding = FragmentTopBinding.inflate(inflater, container, false).apply {
            pager.adapter = adapter
            pager.offscreenPageLimit = 2
            pager.addOnPageChangeListener(
                onPageSelected = { activity!!.title = adapter.getPageTitle(it) },
                onPageScrolled = { _, _, _ -> },
                onPageScrollStateChanged = {}
            )
        }
        binding.postStatusButtonListener = getPostStatusButtonListener()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        topViewModel.detailActionEvent.observe(this, Observer {
            it ?: return@Observer
            requireActivity().run {
                val intent = StatusDetailActivity.createIntent(this, it.id)
                startActivity(intent)
            }
        })
        topViewModel.replyActionEvent.observe(this, Observer {
            it ?: return@Observer
            requireActivity().run {
                val intent = PostStatusActivity
                        .createReplyIntent(this, it.account!!.acct, it.id)
                startActivity(intent)
            }
        })
        topViewModel.favoriteActionEvent.observe(this, Observer {
            Toast.makeText(activity, getString(R.string.favorited), Toast.LENGTH_SHORT).show()
        })
        topViewModel.unfavoriteActionEvent.observe(this, Observer {
            Toast.makeText(activity, getString(R.string.unfavorite), Toast.LENGTH_SHORT).show()
        })
        topViewModel.reblogActionEvent.observe(this, Observer {
            Toast.makeText(activity, getString(R.string.reblogged), Toast.LENGTH_SHORT).show()
        })
        topViewModel.menuActionEvent.observe(this, Observer {
            it ?: return@Observer
            val fragment = MenuDialogFragment.newInstance(it)
            fragment.show(fragmentManager, "menu")
        })
        topViewModel.favoriteError.observe(this, Observer {
            Toast.makeText(activity, R.string.error, Toast.LENGTH_SHORT).show()
        })
        topViewModel.reblogError.observe(this, Observer {
            Toast.makeText(activity, R.string.error, Toast.LENGTH_SHORT).show()
        })
        topViewModel.openAccountEvent.observe(this, Observer {
            requireActivity().run {
                val intent = ProfileActivity.createIntent(this, it.id)
                startActivity(intent)
            }
        })
        topViewModel.openStatusEvent.observe(this, Observer {
            requireActivity().run {
                val intent = StatusDetailActivity.createIntent(this, it.id)
                startActivity(intent)
            }
        })
        topViewModel.openUserAccountEvent.observe(this, Observer {
            requireActivity().run {
                val intent = ProfileActivity.createIntent(this, it.id)
                startActivity(intent)
            }
        })
    }

    private fun getPostStatusButtonListener(): View.OnClickListener {
        return View.OnClickListener {
            val intent = Intent(activity, PostStatusActivity::class.java)
            requireActivity().run {
                startActivity(intent)
            }
        }
    }

    override fun onDestroyView() {
        topViewModel.shutdownUserStream()
        super.onDestroyView()
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.navigation_home -> {
                binding.pager.currentItem = 0
                return true
            }
            R.id.navigation_notification -> {
                binding.pager.currentItem = 1
                return true
            }
            R.id.navigation_local_timeline -> {
                binding.pager.currentItem = 2
                return true
            }
            R.id.navigation_profile -> {
                topViewModel.acitonOpenUserProfile()
                return true
            }
        }
        return false
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