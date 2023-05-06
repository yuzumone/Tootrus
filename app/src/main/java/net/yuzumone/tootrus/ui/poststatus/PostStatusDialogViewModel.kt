package net.yuzumone.tootrus.ui.poststatus

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sys1yagi.mastodon4j.api.entity.Status
import net.yuzumone.tootrus.service.PostStatusService

class PostStatusDialogViewModel : ViewModel() {

    val text = MutableLiveData<String>()
    val spoilerText = MutableLiveData<String>()
    val statusVisibility = MutableLiveData<Status.Visibility>()
    val imageUris = MutableLiveData<List<String>>()
    val isSensitive = MutableLiveData<Boolean>()
    val spoilerTextVisibility = MutableLiveData<Boolean>()
    val draft = MutableLiveData<PostStatusService.Params>()
    val repliedStatus = MutableLiveData<Status>()
    val eventNavigationClick = MutableLiveData<Unit>()

    init {
        statusVisibility.value = Status.Visibility.Public
        spoilerTextVisibility.value = false
    }

    fun setRepliedStatus(status: Status?) {
        status?.let { s ->
            repliedStatus.value = s
            val v = Status.Visibility.values().first { it.value == s.visibility }
            statusVisibility.value = v
        }
    }

    fun setImageUris(uris: List<String>) {
        imageUris.value = uris
    }

    fun setSensitive(sensitive: Boolean) {
        isSensitive.value = sensitive
    }

    fun setSpoilerTextVisibility(visibility: Boolean) {
        spoilerTextVisibility.value = visibility
    }

    fun setStatusVisibility(visibility: Status.Visibility) {
        statusVisibility.value = visibility
    }

    fun onNavigationClick(): View.OnClickListener {
        return View.OnClickListener {
            eventNavigationClick.value = Unit
        }
    }

    fun postStatus() {
        val text = text.value ?: ""
        val inReplyToId = repliedStatus.value?.id
        val imageUris = imageUris.value
        val sensitive = isSensitive.value ?: false
        val spoilerText = spoilerText.value ?: ""
        val visibility = statusVisibility.value ?: Status.Visibility.Public
        draft.value =
            PostStatusService.Params(
                text,
                inReplyToId,
                imageUris,
                sensitive,
                spoilerText,
                visibility
            )
    }
}
