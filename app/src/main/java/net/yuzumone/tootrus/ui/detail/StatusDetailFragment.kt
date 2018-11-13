package net.yuzumone.tootrus.ui.detail

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.sys1yagi.mastodon4j.api.entity.Status
import net.yuzumone.tootrus.databinding.FragmentStatusDetailBinding

class StatusDetailFragment : Fragment() {

    private lateinit var binding: FragmentStatusDetailBinding
    private val status by lazy {
        Gson().fromJson(arguments!!.getString(ARG_STATUS), Status::class.java)
    }

    companion object {
        private const val ARG_STATUS = "status"
        fun newInstance(status: Status): StatusDetailFragment {
            return StatusDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_STATUS, Gson().toJson(status))
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentStatusDetailBinding.inflate(inflater, container, false).also {
            it.status = if (status.reblog == null) status else status.reblog
        }
        return binding.root
    }
}