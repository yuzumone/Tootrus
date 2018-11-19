package net.yuzumone.tootrus.ui.common

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.AsyncTask
import android.util.AttributeSet
import android.util.LruCache
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import net.yuzumone.tootrus.R
import net.yuzumone.tootrus.databinding.ViewWebCardBinding
import org.jsoup.HttpStatusException
import org.jsoup.Jsoup
import org.jsoup.UnsupportedMimeTypeException

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
        doc.select("a").forEach {
            if (!it.attr("class").contains("mention")) {
                val url = it.attr("href")
                val v = bind(url)
                v.setOnClickListener { _ ->
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    context.startActivity(intent)
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
            it.card = WebCard(url, "")
        }
        val card = WebCardCache.getInstance().getCard(url)
        if (card != null) {
            binding.card = card
        } else {
            WebCardTask(object : WebCardTask.WebCardListener {
                override fun run(card: WebCard?) {
                    binding.card = card
                }
            }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url)
        }
        return binding.root
    }
}

data class WebCard(
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

class WebCardTask(private val listener: WebCardListener) : AsyncTask<String, Unit, WebCard>() {

    interface WebCardListener {
        fun run(card: WebCard?)
    }

    override fun doInBackground(vararg params: String?): WebCard {
        return try {
            val doc = Jsoup.connect(params[0]).followRedirects(true).get()
            val title = doc.title()
            val imageUrl =
                    if (doc.select("meta[name=twitter:image]").attr("content").isBlank())
                        doc.select("meta[property=og:image]").attr("content") else
                        doc.select("meta[name=twitter:image]").attr("content")
            val card = WebCard(title, imageUrl)
            WebCardCache.getInstance().putCard(params[0]!!, card)
            card
        } catch (e: UnsupportedMimeTypeException) {
            val card = WebCard(params[0]!!, "")
            WebCardCache.getInstance().putCard(params[0]!!, card)
            card
        } catch (e: HttpStatusException) {
            val card = WebCard(params[0]!!, "")
            WebCardCache.getInstance().putCard(params[0]!!, card)
            card
        }
    }

    override fun onPostExecute(result: WebCard?) {
        listener.run(result)
    }
}