package net.yuzumone.tootrus.ui.top

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import dagger.android.support.AndroidSupportInjection
import net.yuzumone.tootrus.R
import net.yuzumone.tootrus.databinding.FragmentTopBinding
import net.yuzumone.tootrus.ui.PostStatusActivity
import net.yuzumone.tootrus.ui.top.local.LocalTimelineFragment
import net.yuzumone.tootrus.ui.top.notification.NotificationFragment
import net.yuzumone.tootrus.ui.top.timeline.TimelineFragment
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
            add(getString(R.string.section_home), TimelineFragment())
            add(getString(R.string.section_notification), NotificationFragment())
            add(getString(R.string.section_local_timeline), LocalTimelineFragment())
        }
        binding = FragmentTopBinding.inflate(inflater, container, false).apply {
            pager.adapter = adapter
            pager.offscreenPageLimit = 2
            pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {

                }

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                    activity!!.title = adapter.getPageTitle(position)
                }

                override fun onPageSelected(position: Int) {

                }
            })
        }
        binding.postStatusButtonListener = getPostStatusButtonListener()
        return binding.root
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