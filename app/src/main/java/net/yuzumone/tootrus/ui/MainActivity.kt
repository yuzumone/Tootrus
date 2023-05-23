package net.yuzumone.tootrus.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import net.yuzumone.tootrus.R
import net.yuzumone.tootrus.databinding.ActivityMainBinding
import net.yuzumone.tootrus.databinding.NavigationHeaderBinding
import net.yuzumone.tootrus.ui.oauth.OAuthFragment
import net.yuzumone.tootrus.ui.top.TopFragment

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var headerBinding: NavigationHeaderBinding
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        headerBinding = DataBindingUtil.inflate(
            layoutInflater, R.layout.navigation_header, binding.navigation, false
        )
        binding.navigation.addHeaderView(headerBinding.root)
        setSupportActionBar(binding.toolbar)
        mainViewModel.eventTransactionToTop.observe(this) {
            val topFragment = TopFragment()
            supportFragmentManager.beginTransaction().replace(R.id.content, topFragment).commit()
            initializeDrawer(topFragment)
            mainViewModel.getCredentials()
        }
        mainViewModel.account.observe(this) {
            headerBinding.account = it
        }
        mainViewModel.setFragment.observe(this) {
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
        }
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
}
