package net.yuzumone.tootrus.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import net.yuzumone.tootrus.R
import net.yuzumone.tootrus.databinding.ActivityFavoriteBinding
import net.yuzumone.tootrus.ui.favorite.FavoriteFragment
import javax.inject.Inject

class FavoriteActivity : AppCompatActivity(), HasSupportFragmentInjector {

    private lateinit var binding: ActivityFavoriteBinding
    @Inject lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_favorite)
        setSupportActionBar(binding.toolbar)
        binding.toolbar.also {
            it.setNavigationIcon(R.drawable.ic_action_back)
            it.setNavigationOnClickListener { finish() }
        }
        if (savedInstanceState == null) {
            val fragment = FavoriteFragment()
            supportFragmentManager.beginTransaction().replace(R.id.content, fragment).commit()
        }
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return dispatchingActivityInjector
    }
}