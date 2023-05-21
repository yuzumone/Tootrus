package net.yuzumone.tootrus.ui.top

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import net.yuzumone.tootrus.R
import net.yuzumone.tootrus.databinding.FragmentTopBinding
import net.yuzumone.tootrus.ui.FavoriteActivity
import net.yuzumone.tootrus.ui.PostStatusActivity
import net.yuzumone.tootrus.ui.ProfileActivity
import net.yuzumone.tootrus.ui.StatusDetailActivity
import net.yuzumone.tootrus.ui.menu.MenuDialogFragment
import net.yuzumone.tootrus.ui.top.home.HomeTimelineFragment
import net.yuzumone.tootrus.ui.top.local.LocalTimelineFragment
import net.yuzumone.tootrus.ui.top.notification.NotificationFragment

@AndroidEntryPoint
class TopFragment : Fragment(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: FragmentTopBinding
    private val topViewModel: TopViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        topViewModel.startUserStream()
        val adapter = ViewPagerAdapter(requireActivity()).apply {
            add(getString(R.string.section_home), HomeTimelineFragment())
            add(getString(R.string.section_notification), NotificationFragment())
            add(getString(R.string.section_local_timeline), LocalTimelineFragment())
        }
        binding = FragmentTopBinding.inflate(inflater, container, false).apply {
            pager.adapter = adapter
            pager.offscreenPageLimit = 2
            pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    requireActivity().title = adapter.getPageTitle(position)
                }
            })
        }
        binding.postStatusButtonListener = getPostStatusButtonListener()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        topViewModel.detailActionEvent.observe(viewLifecycleOwner, Observer {
            Log.d("detaiAction", "called")
            Log.d("detailActionEvent", it.toString())
            it ?: return@Observer
            requireActivity().run {
                val intent = StatusDetailActivity.createIntent(this, it.id)
                startActivity(intent)
            }
        })
        topViewModel.replyActionEvent.observe(viewLifecycleOwner, Observer {
            it ?: return@Observer
            requireActivity().run {
                val intent = PostStatusActivity.createReplyIntent(this, it)
                startActivity(intent)
            }
        })
        topViewModel.favoriteActionEvent.observe(viewLifecycleOwner) {
            Toast.makeText(activity, getString(R.string.favorited), Toast.LENGTH_SHORT).show()
        }
        topViewModel.unfavoriteActionEvent.observe(viewLifecycleOwner) {
            Toast.makeText(activity, getString(R.string.unfavorite), Toast.LENGTH_SHORT).show()
        }
        topViewModel.reblogActionEvent.observe(viewLifecycleOwner) {
            Toast.makeText(activity, getString(R.string.reblogged), Toast.LENGTH_SHORT).show()
        }
        topViewModel.menuActionEvent.observe(viewLifecycleOwner, Observer {
            it ?: return@Observer
            val fragment = MenuDialogFragment.newInstance(it)
            fragment.show(parentFragmentManager, "menu")
        })
        topViewModel.favoriteError.observe(viewLifecycleOwner) {
            Toast.makeText(activity, R.string.error, Toast.LENGTH_SHORT).show()
        }
        topViewModel.reblogError.observe(viewLifecycleOwner) {
            Toast.makeText(activity, R.string.error, Toast.LENGTH_SHORT).show()
        }
        topViewModel.openAccountEvent.observe(viewLifecycleOwner) {
            requireActivity().run {
                val intent = ProfileActivity.createIntent(this, it.id)
                startActivity(intent)
            }
        }
        topViewModel.openStatusEvent.observe(viewLifecycleOwner) {
            requireActivity().run {
                val intent = StatusDetailActivity.createIntent(this, it.id)
                startActivity(intent)
            }
        }
        topViewModel.openUserAccountEvent.observe(viewLifecycleOwner) {
            requireActivity().run {
                val intent = ProfileActivity.createIntent(this, it.id)
                startActivity(intent)
            }
        }
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
            R.id.navigation_favorite -> {
                val intent = Intent(activity, FavoriteActivity::class.java)
                requireActivity().run {
                    startActivity(intent)
                }
            }
        }
        return false
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
