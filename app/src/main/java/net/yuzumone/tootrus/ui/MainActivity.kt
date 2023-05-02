package net.yuzumone.tootrus.ui

import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.navigation.NavigationView
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import net.yuzumone.tootrus.R
import net.yuzumone.tootrus.databinding.ActivityMainBinding
import net.yuzumone.tootrus.databinding.NavigationHeaderBinding
import net.yuzumone.tootrus.ui.oauth.OAuthFragment
import net.yuzumone.tootrus.ui.top.TopFragment
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasSupportFragmentInjector {

    private lateinit var binding: ActivityMainBinding
    private lateinit var headerBinding: NavigationHeaderBinding
    private lateinit var mainViewModel: MainViewModel

    @Inject
    lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        headerBinding = DataBindingUtil.inflate(
            layoutInflater, R.layout.navigation_header, binding.navigation, false
        )
        binding.navigation.addHeaderView(headerBinding.root)
        setSupportActionBar(binding.toolbar)
        mainViewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        mainViewModel.eventTransactionToTop.observe(this, Observer {
            val topFragment = TopFragment()
            supportFragmentManager.beginTransaction().replace(R.id.content, topFragment).commit()
            initializeDrawer(topFragment)
            mainViewModel.getCredentials()
        })
        mainViewModel.account.observe(this, Observer {
            headerBinding.account = it
        })
        mainViewModel.setFragment.observe(this, Observer {
            when (it) {
                SetFragment.OAUTH -> {
                    supportFragmentManager.beginTransaction().replace(R.id.content, OAuthFragment())
                        .commit()
                }
                SetFragment.TOP -> {
                    val topFragment = TopFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.content, topFragment)
                        .commit()
                    initializeDrawer(topFragment)
                    mainViewModel.getCredentials()
                }
                else -> {
                    // NOPE
                }
            }
        })
    }

    private fun initializeDrawer(listener: NavigationView.OnNavigationItemSelectedListener) {
        val drawerToggle = ActionBarDrawerToggle(
            this,
            binding.drawer,
            binding.toolbar,
            R.string.open_drawer_content,
            R.string.close_drawer_content
        )
        binding.drawer.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
        binding.navigation.inflateMenu(R.menu.navigation)
        binding.navigation.setNavigationItemSelectedListener {
            binding.drawer.closeDrawer(GravityCompat.START)
            listener.onNavigationItemSelected(it)
        }
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return dispatchingActivityInjector
    }
}
