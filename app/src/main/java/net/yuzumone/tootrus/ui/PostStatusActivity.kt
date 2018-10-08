package net.yuzumone.tootrus.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import net.yuzumone.tootrus.ui.poststatus.PostStatusDialogFragment

class PostStatusActivity : AppCompatActivity() {

    private val inReplyToAcct: String by lazy { intent.getStringExtra(ARG_IN_REPLY_TO_ACCT) }
    private val inReplyToId: Long by lazy { intent.getLongExtra(ARG_IN_REPLY_TO_ID, 0L) }

    companion object {
        private const val ARG_IN_REPLY_TO_ACCT = "in_reply_to_acct"
        private const val ARG_IN_REPLY_TO_ID = "in_reply_to_id"
        fun createReplyIntent(content: Context, inReplyToAcct: String, inReplyToId: Long): Intent {
            return Intent(content, PostStatusActivity::class.java).apply {
                putExtra(ARG_IN_REPLY_TO_ACCT, inReplyToAcct)
                putExtra(ARG_IN_REPLY_TO_ID, inReplyToId)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            if (inReplyToId != 0L) {
                val dialog = PostStatusDialogFragment.newReplyInstance(inReplyToAcct, inReplyToId)
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