package net.yuzumone.tootrus.ui.oauth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import net.yuzumone.tootrus.R
import net.yuzumone.tootrus.databinding.FragmentOauthBinding
import net.yuzumone.tootrus.ui.MainViewModel

@AndroidEntryPoint
class OAuthFragment : Fragment() {

    private lateinit var binding: FragmentOauthBinding
    private val oauthViewModel: OAuthViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOauthBinding.inflate(inflater, container, false).apply {
            viewModel = this@OAuthFragment.oauthViewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        oauthViewModel.oauthParameter.observe(viewLifecycleOwner) {
            it?.url.let { url ->
                requireActivity().run {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                }
            }
        }
        oauthViewModel.oauthParameterError.observe(viewLifecycleOwner) {
            binding.inputInstanceName.error = getString(R.string.error)
        }
        oauthViewModel.transactionMainView.observe(viewLifecycleOwner) {
            mainViewModel.eventTransactionToTop.value = Unit
        }
        oauthViewModel.accessTokenError.observe(viewLifecycleOwner) {
            binding.inputOauthCode.error = getString(R.string.error)
        }
    }
}
