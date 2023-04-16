package net.yuzumone.tootrus.ui.favorite

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.android.support.AndroidSupportInjection
import net.yuzumone.tootrus.R
import net.yuzumone.tootrus.databinding.FragmentFavoriteBinding
import net.yuzumone.tootrus.ui.StatusDetailActivity
import net.yuzumone.tootrus.ui.common.StatusBindingAdapter
import javax.inject.Inject

class FavoriteFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var viewModel: FavoriteViewModel
    private lateinit var adapter: StatusBindingAdapter
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewModel = ViewModelProviders.of(requireActivity(), viewModelFactory)
                .get(FavoriteViewModel::class.java)
        adapter = StatusBindingAdapter(viewModel)
        val layoutManager = LinearLayoutManager(activity)
        val divider = DividerItemDecoration(activity, layoutManager.orientation)
        divider.setDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.divider)!!)
        binding = FragmentFavoriteBinding.inflate(inflater, container, false).also {
            it.recyclerFavorite.adapter = adapter
            it.recyclerFavorite.layoutManager = layoutManager
            it.recyclerFavorite.addItemDecoration(divider)
            it.recyclerFavorite.itemAnimator = null
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.favorites.observe(viewLifecycleOwner, Observer {
            if (binding.progress.visibility == View.VISIBLE) {
                binding.progress.visibility = View.GONE
            }
            adapter.update(it)
        })
        viewModel.error.observe(viewLifecycleOwner, Observer {
            if (binding.progress.visibility == View.VISIBLE) {
                binding.progress.visibility = View.GONE
            }
            Toast.makeText(activity, R.string.error, Toast.LENGTH_SHORT).show()
        })
        viewModel.eventOpenStatus.observe(viewLifecycleOwner, Observer {
            requireActivity().run {
                val intent = StatusDetailActivity.createIntent(this, it.id)
                startActivity(intent)
            }
        })
    }
}