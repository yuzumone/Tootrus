package net.yuzumone.tootrus.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import net.yuzumone.tootrus.R
import net.yuzumone.tootrus.databinding.ActivityMainBinding
import net.yuzumone.tootrus.ui.oauth.OAuthFragment
import net.yuzumone.tootrus.ui.top.TopFragment
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasSupportFragmentInjector {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    @Inject lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Fragment>
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(binding.toolbar)
        mainViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(MainViewModel::class.java)
        if (savedInstanceState == null) {
            mainViewModel.setFragment.observe(this, Observer {
                when (it) {
                    SetFragment.OAUTH -> {
                        supportFragmentManager.beginTransaction()
                                .add(R.id.content, OAuthFragment()).commit()
                    }
                    SetFragment.TOP -> {
                        val topFragment = TopFragment()
                        supportFragmentManager.beginTransaction()
                                .add(R.id.content, topFragment).commit()
                        initializeDrawer(topFragment)
                    }
                }
            })
        }
    }

    private fun initializeDrawer(listener: NavigationView.OnNavigationItemSelectedListener) {
        val drawerToggle = ActionBarDrawerToggle(this, binding.drawer, binding.toolbar,
                R.string.open_drawer_content, R.string.close_drawer_content)
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
