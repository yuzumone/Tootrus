package net.yuzumone.tootrus.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.gson.Gson
import com.sys1yagi.mastodon4j.api.entity.Attachment
import net.yuzumone.tootrus.R
import net.yuzumone.tootrus.databinding.ActivityMediaPreviewBinding
import net.yuzumone.tootrus.ui.preview.ImagePreviewFragment

class MediaPreviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMediaPreviewBinding
    private lateinit var adapter: ViewPagerAdapter
    private val media by lazy {
        Gson().fromJson(
            intent.getStringExtra(ARG_MEDIA),
            Media::class.java
        )
    }

    companion object {
        private const val ARG_MEDIA = "media"
        fun createIntent(context: Context, media: Media): Intent {
            return Intent(context, MediaPreviewActivity::class.java).apply {
                putExtra(ARG_MEDIA, Gson().toJson(media))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_media_preview)
        adapter = ViewPagerAdapter(this)
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
            it.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    binding.toolbar.title = adapter.getPageTitle(position)
                }
            })
        }
    }

    class ViewPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

        private val fragments = ArrayList<Fragment>()

        override fun getItemCount(): Int = fragments.size

        override fun createFragment(position: Int): Fragment = fragments[position]

        fun getPageTitle(position: Int): String = "${position + 1} of ${fragments.size}"

        fun add(fragment: Fragment) {
            fragments.add(fragment)
        }
    }

    data class Media(
        val attachments: List<Attachment>,
        val index: Int
    )
}
