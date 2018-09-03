package net.yuzumone.tootrus.ui.top

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.yuzumone.tootrus.databinding.FragmentTopBinding
import net.yuzumone.tootrus.ui.PostStatusActivity
import net.yuzumone.tootrus.ui.top.timeline.TimelineFragment

class TopFragment : Fragment() {

    private lateinit var binding: FragmentTopBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val adapter = ViewPagerAdapter(childFragmentManager).apply {
            add("HomeTimeline", TimelineFragment())
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