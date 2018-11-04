package net.yuzumone.tootrus.ui

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import com.google.gson.Gson
import com.sys1yagi.mastodon4j.api.entity.Attachment
import net.yuzumone.tootrus.R
import net.yuzumone.tootrus.databinding.ActivityMediaPreviewBinding
import net.yuzumone.tootrus.ui.preview.ImagePreviewFragment
import net.yuzumone.tootrus.util.addOnPageChangeListener

class MediaPreviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMediaPreviewBinding
    private lateinit var adapter: ViewPagerAdapter
    private val media by lazy { Gson().fromJson(intent.getStringExtra(ARG_MEDIA), Media::class.java) }

    companion object {
        private const val ARG_MEDIA = "media"
        fun createIntent(context: Context, media: Media) : Intent {
            return Intent(context, MediaPreviewActivity::class.java).apply {
                putExtra(ARG_MEDIA, Gson().toJson(media))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_media_preview)
        adapter = ViewPagerAdapter(supportFragmentManager)
        media.attachments.forEach {
            adapter.add(ImagePreviewFragment.newInstance(it))
        }
        binding.toolbar.apply {
            title = adapter.getPageTitle(media.index)
            setNavigationIcon(R.drawable.ic_action_back)
            setNavigationOnClickListener { finish() }
        }
        binding.pager.also { it ->
            it.adapter = adapter
            it.currentItem = media.index
            it.addOnPageChangeListener(
                    onPageSelected = { binding.toolbar.title = adapter.getPageTitle(it) },
                    onPageScrolled = { _, _, _ -> },
                    onPageScrollStateChanged = {}
            )
        }
    }

    class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        private val fragments = ArrayList<Fragment>()

        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getCount(): Int {
            return fragments.size
        }

        override fun getPageTitle(position: Int): CharSequence {
            val p = position + 1
            val size = fragments.size
            return "$p of $size"
        }

        fun add(fragment: Fragment) {
            fragments.add(fragment)
            notifyDataSetChanged()
        }
    }

    data class Media(
            val attachments: List<Attachment>,
            val index: Int
    )
}