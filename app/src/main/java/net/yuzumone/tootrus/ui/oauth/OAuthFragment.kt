package net.yuzumone.tootrus.ui.oauth

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import dagger.android.support.AndroidSupportInjection
import net.yuzumone.tootrus.R
import net.yuzumone.tootrus.databinding.FragmentOauthBinding
import net.yuzumone.tootrus.ui.MainViewModel
import javax.inject.Inject

class OAuthFragment : Fragment() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var binding: FragmentOauthBinding
    private lateinit var oauthViewModel: OAuthViewModel

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        oauthViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(OAuthViewModel::class.java)
        binding = FragmentOauthBinding.inflate(inflater, container, false).apply {
            viewModel = this@OAuthFragment.oauthViewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        oauthViewModel.oauthParameter.observe(viewLifecycleOwner, Observer {
            it?.url.let { url ->
                requireActivity().run {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                }
            }
        })
        oauthViewModel.oauthParameterError.observe(viewLifecycleOwner, Observer {
            binding.inputInstanceName.error = getString(R.string.error)
        })
        oauthViewModel.transactionMainView.observe(viewLifecycleOwner, Observer {
            val mainViewModel = ViewModelProviders.of(requireActivity(), viewModelFactory)
                    .get(MainViewModel::class.java)
            mainViewModel.eventTransactionToTop.value = Unit
        })
        oauthViewModel.accessTokenError.observe(viewLifecycleOwner, Observer {
            binding.inputOauthCode.error = getString(R.string.error)
        })
    }
}