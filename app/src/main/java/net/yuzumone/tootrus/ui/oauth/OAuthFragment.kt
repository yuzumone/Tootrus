package net.yuzumone.tootrus.ui.oauth

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.AndroidSupportInjection
import net.yuzumone.tootrus.R
import net.yuzumone.tootrus.databinding.FragmentOauthBinding
import net.yuzumone.tootrus.ui.top.TopFragment
import javax.inject.Inject

class OAuthFragment : Fragment() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var binding: FragmentOauthBinding
    private lateinit var viewModel: OAuthViewModel

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentOauthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(OAuthViewModel::class.java)
        binding.viewModel = viewModel
        viewModel.oauthParameter.observe(this, Observer {
            it?.url.let { url ->
                requireActivity().run {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                }
            }
        })
        viewModel.oauthParameterError.observe(this, Observer {
            binding.inputInstanceName.error = getString(R.string.error)
        })
        viewModel.transactionMainView.observe(this, Observer {
            requireFragmentManager().run {
                beginTransaction().replace(R.id.content, TopFragment()).commit()
            }
        })
        viewModel.accessTokenError.observe(this, Observer {
            binding.inputOauthCode.error = getString(R.string.error)
        })
    }
}