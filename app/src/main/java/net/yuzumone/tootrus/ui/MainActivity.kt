package net.yuzumone.tootrus.ui

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import net.yuzumone.tootrus.R
import net.yuzumone.tootrus.databinding.ActivityMainBinding
import net.yuzumone.tootrus.ui.oauth.OAuthFragment
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasSupportFragmentInjector {

    private lateinit var binding: ActivityMainBinding
    @Inject lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(binding.toolbar)
        if (savedInstanceState == null) {
            val fragment = OAuthFragment()
            supportFragmentManager.beginTransaction().add(R.id.content, fragment).commit()
        }
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return dispatchingActivityInjector
    }
}
