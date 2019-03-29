package net.yuzumone.tootrus.ui.poststatus

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sys1yagi.mastodon4j.api.entity.Status

class PostStatusDialogViewModel : ViewModel() {

    val visibility = MutableLiveData<Status.Visibility>()

    fun updateVisibilityPublic() {
        visibility.value = Status.Visibility.Public
    }

    fun updateVisibilityUnlisted() {
        visibility.value = Status.Visibility.Unlisted
    }

    fun updateVisibilityPrivate() {
        visibility.value = Status.Visibility.Private
    }

    fun updateVisibilityDirect() {
        visibility.value = Status.Visibility.Direct
    }
}