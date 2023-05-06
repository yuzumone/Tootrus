package net.yuzumone.tootrus.ui.preview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.sys1yagi.mastodon4j.api.entity.Attachment
import net.yuzumone.tootrus.databinding.FragmentImagePreviewBinding

class ImagePreviewFragment : Fragment() {

    private lateinit var binding: FragmentImagePreviewBinding
    private val attachment by lazy {
        Gson().fromJson(requireArguments().getString(ARG_ATTACHMENT), Attachment::class.java)
    }

    companion object {
        private const val ARG_ATTACHMENT = "attachment"
        fun newInstance(attachment: Attachment): ImagePreviewFragment {
            return ImagePreviewFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_ATTACHMENT, Gson().toJson(attachment))
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentImagePreviewBinding.inflate(inflater, container, false)
        binding.attachment = attachment
        return binding.root
    }
}
