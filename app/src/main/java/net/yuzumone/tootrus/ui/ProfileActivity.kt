package net.yuzumone.tootrus.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import net.yuzumone.tootrus.R
import net.yuzumone.tootrus.databinding.ActivityProfileBinding
import net.yuzumone.tootrus.ui.profile.ProfileFragment
import net.yuzumone.tootrus.ui.profile.ProfileViewModel
import javax.inject.Inject

class ProfileActivity : AppCompatActivity(), HasSupportFragmentInjector {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var profileViewModel: ProfileViewModel
    private val id: Long by lazy { intent.getLongExtra(ARG_ID, 0L) }
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Fragment>

    companion object {
        private const val ARG_ID = "id"
        fun createIntent(context: Context, id: Long): Intent {
            return Intent(context, ProfileActivity::class.java).apply {
                putExtra(ARG_ID, id)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile)
        profileViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(ProfileViewModel::class.java)
        profileViewModel.accountAndRelationship.observe(this, Observer {
            binding.progress.visibility = View.GONE
            if (it != null) {
                val fragment = ProfileFragment.newInstance(it.first, it.second)
                supportFragmentManager.beginTransaction().add(android.R.id.content, fragment).commit()
            } else {
                Toast.makeText(this, R.string.error, Toast.LENGTH_SHORT).show()
                finish()
            }
        })
        if (savedInstanceState == null) {
            profileViewModel.getAccountAndRelationShip(id)
        }
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return dispatchingActivityInjector
    }
}