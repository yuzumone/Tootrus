package net.yuzumone.tootrus.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import net.yuzumone.tootrus.R
import net.yuzumone.tootrus.databinding.ActivityStatusDetailBinding
import net.yuzumone.tootrus.ui.detail.StatusDetailFragment
import net.yuzumone.tootrus.ui.detail.StatusDetailViewModel
import javax.inject.Inject

class StatusDetailActivity : AppCompatActivity(), HasSupportFragmentInjector {

    private lateinit var binding: ActivityStatusDetailBinding
    private lateinit var statusDetailViewModel: StatusDetailViewModel
    private val id: Long by lazy { intent.getLongExtra(ARG_ID, 0L) }
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
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
        binding.toolbar.setNavigationIcon(R.drawable.ic_action_back)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        statusDetailViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(StatusDetailViewModel::class.java)
        statusDetailViewModel.status.observe(this, Observer { status ->
            binding.progress.visibility = View.GONE
            if (status != null) {
                val fragment = StatusDetailFragment.newInstance(status)
                supportFragmentManager.beginTransaction().add(R.id.content, fragment).commit()
            } else {
                Toast.makeText(this, R.string.error, Toast.LENGTH_SHORT).show()
                finish()
            }
        })
        if (savedInstanceState == null) {
            statusDetailViewModel.getStatus(id)
        }
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return dispatchingActivityInjector
    }
}