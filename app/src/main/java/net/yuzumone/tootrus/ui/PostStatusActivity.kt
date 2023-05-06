package net.yuzumone.tootrus.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.sys1yagi.mastodon4j.api.entity.Status
import net.yuzumone.tootrus.ui.poststatus.PostStatusDialogFragment

class PostStatusActivity : AppCompatActivity() {

    private val repliedStatus: Status? by lazy {
        Gson().fromJson(intent.getStringExtra(ARG_REPLIED_STATUS), Status::class.java)
    }

    companion object {
        private const val ARG_REPLIED_STATUS = "replied_status"
        fun createReplyIntent(content: Context, status: Status): Intent {
            return Intent(content, PostStatusActivity::class.java).apply {
                putExtra(ARG_REPLIED_STATUS, Gson().toJson(status))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            if (repliedStatus != null) {
                val dialog = PostStatusDialogFragment.newReplyInstance(repliedStatus!!)
                dialog.show(supportFragmentManager, "PostStatus")
            } else {
                val dialog = PostStatusDialogFragment()
                dialog.show(supportFragmentManager, "PostStatus")
            }
        }
    }

    override fun finish() {
        overridePendingTransition(0, 0)
        super.finish()
    }
}
