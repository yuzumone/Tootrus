package net.yuzumone.tootrus.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sys1yagi.mastodon4j.api.entity.Status
import net.yuzumone.tootrus.domain.Failure
import net.yuzumone.tootrus.domain.Success
import net.yuzumone.tootrus.domain.mastodon.status.GetStatusUseCase
import net.yuzumone.tootrus.domain.mastodon.status.PostFavoriteUseCase
import net.yuzumone.tootrus.domain.mastodon.status.PostReblogUseCase
import net.yuzumone.tootrus.domain.mastodon.status.PostUnfavoriteUseCase
import javax.inject.Inject

class StatusDetailViewModel @Inject constructor(
        private val getStatusUseCase: GetStatusUseCase,
        private val postFavoriteUseCase: PostFavoriteUseCase,
        private val postUnfavoriteUseCase: PostUnfavoriteUseCase,
        private val postReblogUseCase: PostReblogUseCase
) : ViewModel(), OnStatusDetailClickListener {

    val status = MutableLiveData<Status>()
    val error = MutableLiveData<Exception>()
    val replyActionEvent = MutableLiveData<Status>()
    val favoriteActionEvent = MutableLiveData<Status>()
    val unfavoriteActionEvent = MutableLiveData<Status>()
    val reblogActionEvent = MutableLiveData<Status>()
    val openMenuActionEvent = MutableLiveData<Status>()
    val favoriteError = MutableLiveData<Exception>()
    val reblogError = MutableLiveData<Exception>()

    fun getStatus(id: Long) {
        getStatusUseCase(id) {
            when (it) {
                is Success -> status.value = it.value
                is Failure -> error.value = it.reason
            }
        }
    }

    private fun postFavorite(target: Status) {
        postFavoriteUseCase(target.id) {
            when (it) {
                is Success -> {
                    status.value = it.value
                    favoriteActionEvent.value = it.value
                }
                is Failure -> favoriteError.value = it.reason
            }
        }
    }

    private fun postUnfavorite(target: Status) {
        postUnfavoriteUseCase(target.id) {
            when (it) {
                is Success -> {
                    status.value = it.value
                    unfavoriteActionEvent.value = it.value
                }
                is Failure -> favoriteError.value = it.reason
            }
        }
    }

    private fun postReblog(target: Status) {
        postReblogUseCase(target.id) {
            when (it) {
                is Success -> {
                    status.value = it.value
                    reblogActionEvent.value = it.value
                }
                is Failure -> reblogError.value = it.reason
            }
        }
    }

    override fun actionReply(status: Status) {
        replyActionEvent.value = status
    }

    override fun actionFavorite(status: Status) {
        if (status.isFavourited) {
            postUnfavorite(status)
        } else {
            postFavorite(status)
        }
    }

    override fun actionReblog(status: Status) {
        if (!status.isReblogged) {
            postReblog(status)
        }
    }

    override fun actionMenu(status: Status) {
        openMenuActionEvent.value = status
    }
}