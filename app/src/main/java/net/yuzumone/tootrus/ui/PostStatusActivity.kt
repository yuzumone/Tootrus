package net.yuzumone.tootrus.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import net.yuzumone.tootrus.ui.poststatus.PostStatusDialogFragment

class PostStatusActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            val dialog = PostStatusDialogFragment()
            dialog.show(supportFragmentManager, "PostStatus")
        }
    }

    override fun finish() {
        overridePendingTransition(0, 0)
        super.finish()
    }
}