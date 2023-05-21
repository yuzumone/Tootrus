package net.yuzumone.tootrus.ui.top.notification

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
import net.yuzumone.tootrus.databinding.FragmentNotificationBinding
import net.yuzumone.tootrus.ui.common.NotificationBindingAdapter
import net.yuzumone.tootrus.ui.top.TopViewModel

@AndroidEntryPoint
class NotificationFragment : Fragment() {

    private lateinit var binding: FragmentNotificationBinding
    private lateinit var adapter: NotificationBindingAdapter
    private val topViewModel: TopViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        adapter = NotificationBindingAdapter(topViewModel)
        val layoutManager = LinearLayoutManager(activity)
        val divider = DividerItemDecoration(activity, layoutManager.orientation)
        divider.setDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.divider)!!)
        binding = FragmentNotificationBinding.inflate(inflater, container, false).apply {
            recyclerNotification.adapter = adapter
            recyclerNotification.layoutManager = layoutManager
            recyclerNotification.addItemDecoration(divider)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        topViewModel.notifications.observe(viewLifecycleOwner) {
            adapter.update(it)
        }
        topViewModel.notificationError.observe(viewLifecycleOwner) {
            Toast.makeText(activity, R.string.error, Toast.LENGTH_SHORT).show()
        }
    }
}