package net.yuzumone.tootrus.ui

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.sys1yagi.mastodon4j.api.entity.Status
import net.yuzumone.tootrus.domain.Failure
import net.yuzumone.tootrus.domain.Success
import net.yuzumone.tootrus.domain.mastodon.status.PostStatusParams
import net.yuzumone.tootrus.domain.mastodon.status.PostStatusUseCase
import javax.inject.Inject

class PostStatusViewModel @Inject constructor(
        private val postStatusUseCase: PostStatusUseCase
): ViewModel() {

    val postStatus = MutableLiveData<Status>()
    val error = MutableLiveData<Exception>()

    fun postStatus(status: String,
                   inReplyToId: Long?,
                   mediaIds: List<Long>?,
                   sensitive: Boolean,
                   spoilerText: String?,
                   visibility: Status.Visibility = Status.Visibility.Public) {
        val params = PostStatusParams(status, inReplyToId, mediaIds, sensitive, spoilerText, visibility)
        postStatusUseCase(params) {
            when (it) {
                is Success -> postStatus.value = it.value
                is Failure -> error.value = it.reason
            }
        }
    }
}