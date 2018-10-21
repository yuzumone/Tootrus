package net.yuzumone.tootrus.ui.detail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.AndroidSupportInjection
import net.yuzumone.tootrus.databinding.FragmentStatusDetailBinding
import javax.inject.Inject

class StatusDetailFragment : Fragment() {

    private lateinit var binding: FragmentStatusDetailBinding
    private lateinit var statusDetailViewModel: StatusDetailViewModel
    private val id by lazy { arguments!!.getLong(ARG_ID) }
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    companion object {
        private const val ARG_ID = "id"
        fun newInstance(id: Long): StatusDetailFragment {
            return StatusDetailFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_ID, id)
                }
            }
        }
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        statusDetailViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(StatusDetailViewModel::class.java)
        binding = FragmentStatusDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        statusDetailViewModel.getStatus(id)
        statusDetailViewModel.status.observe(this, Observer {
            binding.status = it
        })
    }
}