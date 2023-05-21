package net.yuzumone.tootrus.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import net.yuzumone.tootrus.R
import net.yuzumone.tootrus.databinding.FragmentFavoriteBinding
import net.yuzumone.tootrus.ui.StatusDetailActivity
import net.yuzumone.tootrus.ui.common.StatusBindingAdapter

@AndroidEntryPoint
class FavoriteFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteBinding
    private val viewModel: FavoriteViewModel by activityViewModels()
    private lateinit var adapter: StatusBindingAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
        viewModel.favorites.observe(viewLifecycleOwner) {
            if (binding.progress.visibility == View.VISIBLE) {
                binding.progress.visibility = View.GONE
            }
            adapter.update(it)
        }
        viewModel.error.observe(viewLifecycleOwner) {
            if (binding.progress.visibility == View.VISIBLE) {
                binding.progress.visibility = View.GONE
            }
            Toast.makeText(activity, R.string.error, Toast.LENGTH_SHORT).show()
        }
        viewModel.eventOpenStatus.observe(viewLifecycleOwner) {
            requireActivity().run {
                val intent = StatusDetailActivity.createIntent(this, it.id)
                startActivity(intent)
            }
        }
    }
}
