package net.yuzumone.tootrus.ui.common

import android.content.Context
import android.net.Uri
import android.os.Looper
import android.util.AttributeSet
import android.util.LruCache
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.core.os.HandlerCompat
import androidx.databinding.DataBindingUtil
import net.yuzumone.tootrus.R
import net.yuzumone.tootrus.databinding.ViewWebCardBinding
import org.jsoup.Jsoup
import java.util.concurrent.Executors

class WebCardView : LinearLayout {

    constructor(context: Context?) :
            this(context, null)

    constructor(context: Context?, attrs: AttributeSet?) :
            this(context, attrs, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
            this(context, attrs, defStyleAttr, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) :
            super(context, attrs, defStyleAttr, defStyleRes) {
        orientation = VERTICAL
    }

    fun setContent(content: String?) {
        val doc = Jsoup.parse(content ?: "")
        val params = CustomTabColorSchemeParams.Builder().apply {
            setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary))
        }.build()
        doc.select("a").forEach {
            if (!it.attr("class").contains("mention")) {
                val url = it.attr("href")
                val v = bind(url)
                v.setOnClickListener {
                    CustomTabsIntent.Builder().apply {
                        setDefaultColorSchemeParams(params)
                    }.build().apply {
                        launchUrl(context, Uri.parse(url))
                    }
                }
                addView(v)
            }
        }
    }

    fun clearCard() {
        removeAllViews()
    }

    private fun bind(url: String): View {
        val binding = DataBindingUtil.inflate<ViewWebCardBinding>(
            LayoutInflater.from(context), R.layout.view_web_card, this, false
        ).also {
            it.card = WebCard(url, "", "")
        }
        val card = WebCardCache.getInstance().getCard(url)
        if (card != null) {
            binding.card = card
        } else {
            val executor = Executors.newSingleThreadExecutor()
            val handler = HandlerCompat.createAsync(Looper.getMainLooper())
            executor.execute {
                val c: WebCard = try {
                    val doc = Jsoup.connect(url).userAgent("bot").followRedirects(true).get()
                    val title = doc.title()
                    val imageUrl = doc.select("meta[property=og:image]").attr("content")
                    WebCard(url, title, imageUrl)
                } catch (e: Exception) {
                    WebCard(url, "", "")
                }
                WebCardCache.getInstance().putCard(url, c)
                handler.post { binding.card = c }
            }
        }
        return binding.root
    }
}

data class WebCard(
    val url: String,
    val title: String,
    val imageUrl: String
)

class WebCardCache private constructor() {

    private val cache: LruCache<String, WebCard>

    init {
        val cacheSize = 5 * 1024 * 1024
        cache = object : LruCache<String, WebCard>(cacheSize) {}
    }

    fun getCard(id: String): WebCard? {
        return cache.get(id)
    }

    fun putCard(url: String, card: WebCard) {
        cache.put(url, card)
    }

    companion object {
        private var instance: WebCardCache? = null
        fun getInstance(): WebCardCache {
            if (instance == null) {
                instance = WebCardCache()
            }
            return instance!!
        }
    }
}
