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
        profileViewModel = ViewModelProvider(this, viewModelFactory)[ProfileViewModel::class.java]
        profileViewModel.accountAndRelationship.observe(this, Observer {
            binding.progress.visibility = View.GONE
            if (it != null) {
                val fragment = ProfileFragment.newInstance(it.first, it.second)
                supportFragmentManager.beginTransaction().replace(android.R.id.content, fragment).commit()
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