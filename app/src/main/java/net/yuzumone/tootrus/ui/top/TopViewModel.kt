package net.yuzumone.tootrus.ui.top

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sys1yagi.mastodon4j.api.Handler
import com.sys1yagi.mastodon4j.api.Range
import com.sys1yagi.mastodon4j.api.entity.Account
import com.sys1yagi.mastodon4j.api.entity.Notification
import com.sys1yagi.mastodon4j.api.entity.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import net.yuzumone.tootrus.domain.Failure
import net.yuzumone.tootrus.domain.Success
import net.yuzumone.tootrus.domain.mastodon.account.GetVerifyCredentialsUseCase
import net.yuzumone.tootrus.domain.mastodon.notification.GetNotificationsUseCase
import net.yuzumone.tootrus.domain.mastodon.status.PostFavoriteUseCase
import net.yuzumone.tootrus.domain.mastodon.status.PostReblogUseCase
import net.yuzumone.tootrus.domain.mastodon.status.PostUnfavoriteUseCase
import net.yuzumone.tootrus.domain.mastodon.stream.ShutdownUserStreamUseCase
import net.yuzumone.tootrus.domain.mastodon.stream.StartUserStreamUseCase
import net.yuzumone.tootrus.domain.mastodon.timeline.GetLocalPublicUseCase
import net.yuzumone.tootrus.domain.mastodon.timeline.GetTimelineUseCase
import net.yuzumone.tootrus.ui.common.OnNotificationAdapterClickListener
import net.yuzumone.tootrus.ui.common.OnStatusAdapterClickListener
import net.yuzumone.tootrus.ui.common.OnStatusAdapterLongClickListener
import net.yuzumone.tootrus.util.insertValues
import net.yuzumone.tootrus.util.postInsertValue
import net.yuzumone.tootrus.util.replaceStatus
import javax.inject.Inject

@HiltViewModel
class TopViewModel @Inject constructor(
    private val startUserStreamUseCase: StartUserStreamUseCase,
    private val shutdownUserStreamUseCase: ShutdownUserStreamUseCase,
    private val postFavoriteUseCase: PostFavoriteUseCase,
    private val postUnfavoriteUseCase: PostUnfavoriteUseCase,
    private val postReblogUseCase: PostReblogUseCase,
    private val getLocalPublicUseCase: GetLocalPublicUseCase,
    private val getVerifyCredentialsUseCase: GetVerifyCredentialsUseCase,
    getTimelineUseCase: GetTimelineUseCase,
    getNotificationsUseCase: GetNotificationsUseCase
) : ViewModel(), OnStatusAdapterClickListener, OnNotificationAdapterClickListener,
    OnStatusAdapterLongClickListener {

    val homeStatuses = MutableLiveData<List<Status>>()
    val localStatuses = MutableLiveData<List<Status>>()
    val notifications = MutableLiveData<List<Notification>>()
    val homeError = MutableLiveData<Exception>()
    val notificationError = MutableLiveData<Exception>()
    val localError = MutableLiveData<Exception>()
    val detailActionEvent = MutableLiveData<Status>()
    val replyActionEvent = MutableLiveData<Status>()
    val favoriteActionEvent = MutableLiveData<Status>()
    val unfavoriteActionEvent = MutableLiveData<Status>()
    val reblogActionEvent = MutableLiveData<Status>()
    val menuActionEvent = MutableLiveData<Status>()
    val favoriteError = MutableLiveData<Exception>()
    val reblogError = MutableLiveData<Exception>()
    val openAccountEvent = MutableLiveData<Account>()
    val openStatusEvent = MutableLiveData<Status>()
    val openUserAccountEvent = MutableLiveData<Account>()

    init {
        val range = Range()
        getTimelineUseCase(range, viewModelScope) {
            when (it) {
                is Success -> homeStatuses.value = it.value
                is Failure -> homeError.value = it.reason
            }
        }
        getNotificationsUseCase(range, viewModelScope) {
            when (it) {
                is Success -> notifications.value = it.value
                is Failure -> notificationError.value = it.reason
            }
        }
        getLocalPublicUseCase(range, viewModelScope) {
            when (it) {
                is Success -> localStatuses.value = it.value
                is Failure -> localError.value = it.reason
            }
        }
    }

    fun startUserStream() {
        val handler = object : Handler {
            override fun onStatus(status: Status) {
                homeStatuses.postInsertValue(status)
            }

            override fun onNotification(notification: Notification) {
                notifications.postInsertValue(notification)
            }

            override fun onDelete(id: Long) {

            }
        }
        startUserStreamUseCase(handler, viewModelScope)
    }

    fun shutdownUserStream() {
        shutdownUserStreamUseCase(Unit, viewModelScope)
    }

    fun updateLocalTimeline() {
        val id = localStatuses.value?.get(0)?.id
        if (id == null) {
            localError.value = NullPointerException()
        } else {
            val range = Range(sinceId = id)
            getLocalPublicUseCase(range, viewModelScope) {
                when (it) {
                    is Success -> localStatuses.insertValues(it.value)
                    is Failure -> localError.value = it.reason
                }
            }
        }
    }

    fun acitonOpenUserProfile() {
        getVerifyCredentialsUseCase(Unit, viewModelScope) {
            when (it) {
                is Success -> {
                    openUserAccountEvent.value = it.value
                }
                is Failure -> TODO()
            }
        }
    }

    private fun postFavorite(target: Status) {
        postFavoriteUseCase(target.id, viewModelScope) {
            when (it) {
                is Success -> {
                    homeStatuses.replaceStatus(target, it.value)
                    localStatuses.replaceStatus(target, it.value)
                    favoriteActionEvent.value = it.value
                }
                is Failure -> favoriteError.value = it.reason
            }
        }
    }

    private fun postUnfavorite(target: Status) {
        postUnfavoriteUseCase(target.id, viewModelScope) {
            when (it) {
                is Success -> {
                    homeStatuses.replaceStatus(target, it.value)
                    localStatuses.replaceStatus(target, it.value)
                    unfavoriteActionEvent.value = it.value
                }
                is Failure -> favoriteError.value = it.reason
            }
        }
    }

    private fun postReblog(target: Status) {
        postReblogUseCase(target.id, viewModelScope) {
            when (it) {
                is Success -> {
                    homeStatuses.replaceStatus(target, it.value)
                    localStatuses.replaceStatus(target, it.value)
                    reblogActionEvent.value = it.value
                }
                is Failure -> reblogError.value = it.reason
            }
        }
    }

    override fun actionDetail(status: Status) {
        Log.d("actionDe", status.toString())
        detailActionEvent.value = status
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
        menuActionEvent.value = status
    }

    override fun onSingleClick(notification: Notification) {
        when (notification.type) {
            Notification.Type.Follow.value -> {
                openAccountEvent.value = notification.account
            }
            Notification.Type.Favourite.value, Notification.Type.Mention.value,
            Notification.Type.Reblog.value -> {
                openStatusEvent.value = notification.status
            }
        }
    }

    override fun onLongClick(status: Status): Boolean {
        openStatusEvent.value = status
        return true
    }
}
