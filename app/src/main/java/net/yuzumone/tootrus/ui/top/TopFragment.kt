package net.yuzumone.tootrus.ui.top

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.AndroidSupportInjection
import net.yuzumone.tootrus.databinding.FragmentTopBinding
import net.yuzumone.tootrus.ui.PostStatusActivity
import net.yuzumone.tootrus.ui.top.notification.NotificationFragment
import net.yuzumone.tootrus.ui.top.timeline.TimelineFragment
import javax.inject.Inject

class TopFragment : Fragment() {

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
            add("HomeTimeline", TimelineFragment())
            add("Notifications", NotificationFragment())
        }
        binding = FragmentTopBinding.inflate(inflater, container, false)
        binding.pager.adapter = adapter
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