package net.yuzumone.tootrus.ui

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import net.yuzumone.tootrus.R
import net.yuzumone.tootrus.databinding.ActivityStatusDetailBinding
import net.yuzumone.tootrus.ui.detail.StatusDetailFragment
import javax.inject.Inject

class StatusDetailActivity : AppCompatActivity(), HasSupportFragmentInjector {

    private lateinit var binding: ActivityStatusDetailBinding
    private val id: Long by lazy { intent.getLongExtra(ARG_ID, 0L) }
    @Inject lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Fragment>

    companion object {
        private const val ARG_ID = "id"
        fun createIntent(context: Context, id: Long): Intent {
            return Intent(context, StatusDetailActivity::class.java).apply {
                putExtra(ARG_ID, id)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_status_detail)
        setSupportActionBar(binding.toolbar)
        if (savedInstanceState == null) {
            val fragment = StatusDetailFragment.newInstance(id)
            supportFragmentManager.beginTransaction().add(R.id.content, fragment).commit()
        }
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return dispatchingActivityInjector
    }
}