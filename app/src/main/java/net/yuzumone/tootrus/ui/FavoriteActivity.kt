package net.yuzumone.tootrus.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import dagger.hilt.android.AndroidEntryPoint
import net.yuzumone.tootrus.R
import net.yuzumone.tootrus.databinding.ActivityFavoriteBinding
import net.yuzumone.tootrus.ui.favorite.FavoriteFragment

@AndroidEntryPoint
class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
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
}
