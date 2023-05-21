package net.yuzumone.tootrus.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import dagger.hilt.android.AndroidEntryPoint
import net.yuzumone.tootrus.R
import net.yuzumone.tootrus.databinding.ActivityProfileBinding
import net.yuzumone.tootrus.ui.profile.ProfileFragment
import net.yuzumone.tootrus.ui.profile.ProfileViewModel

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private val profileViewModel: ProfileViewModel by viewModels()
    private val id: Long by lazy { intent.getLongExtra(ARG_ID, 0L) }

    companion object {
        private const val ARG_ID = "id"
        fun createIntent(context: Context, id: Long): Intent {
            return Intent(context, ProfileActivity::class.java).apply {
                putExtra(ARG_ID, id)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile)
        profileViewModel.accountAndRelationship.observe(this) {
            binding.progress.visibility = View.GONE
            if (it != null) {
                val fragment = ProfileFragment.newInstance(it.first, it.second)
                supportFragmentManager.beginTransaction().replace(android.R.id.content, fragment)
                    .commit()
            } else {
                Toast.makeText(this, R.string.error, Toast.LENGTH_SHORT).show()
                finish()
            }
        }
        if (savedInstanceState == null) {
            profileViewModel.getAccountAndRelationShip(id)
        }
    }
}
