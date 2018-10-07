package net.yuzumone.tootrus.ui.top

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.sys1yagi.mastodon4j.api.Handler
import com.sys1yagi.mastodon4j.api.Range
import com.sys1yagi.mastodon4j.api.entity.Notification
import com.sys1yagi.mastodon4j.api.entity.Status
import net.yuzumone.tootrus.domain.Failure
import net.yuzumone.tootrus.domain.Success
import net.yuzumone.tootrus.domain.mastodon.notification.GetNotificationsUseCase
import net.yuzumone.tootrus.domain.mastodon.status.PostFavoriteUseCase
import net.yuzumone.tootrus.domain.mastodon.status.PostReblogUseCase
import net.yuzumone.tootrus.domain.mastodon.status.PostUnfavoriteUseCase
import net.yuzumone.tootrus.domain.mastodon.stream.ShutdownUserStreamUseCase
import net.yuzumone.tootrus.domain.mastodon.stream.StartUserStreamUseCase
import net.yuzumone.tootrus.domain.mastodon.timeline.GetLocalPublicUseCase
import net.yuzumone.tootrus.domain.mastodon.timeline.GetTimelineUseCase
import net.yuzumone.tootrus.util.insertValues
import net.yuzumone.tootrus.util.postInsertValue
import net.yuzumone.tootrus.util.replaceValue
import javax.inject.Inject

class TopViewModel @Inject constructor(
        private val startUserStreamUseCase: StartUserStreamUseCase,
        private val shutdownUserStreamUseCase: ShutdownUserStreamUseCase,
        private val postFavoriteUseCase: PostFavoriteUseCase,
        private val postUnfavoriteUseCase: PostUnfavoriteUseCase,
        private val postReblogUseCase: PostReblogUseCase,
        private val getLocalPublicUseCase: GetLocalPublicUseCase,
        getTimelineUseCase: GetTimelineUseCase,
        getNotificationsUseCase: GetNotificationsUseCase
): ViewModel() {

    val homeStatuses = MutableLiveData<List<Status>>()
    val localStatuses = MutableLiveData<List<Status>>()
    val favoritedStatus = MutableLiveData<Status>()
    val unfavoriteStatus = MutableLiveData<Status>()
    val rebloggedStatus = MutableLiveData<Status>()
    val notifications = MutableLiveData<List<Notification>>()
    val error = MutableLiveData<Exception>()

    init {
        val range = Range()
        getTimelineUseCase(range) {
            when (it) {
                is Success -> homeStatuses.value = it.value
                is Failure -> error.value = it.reason
            }
        }
        getNotificationsUseCase(range) {
            when (it) {
                is Success -> notifications.value = it.value
                is Failure -> error.value = it.reason
            }
        }
        getLocalPublicUseCase(range) {
            when (it) {
                is Success -> localStatuses.value = it.value
                is Failure -> error.value = it.reason
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
        startUserStreamUseCase(handler)
    }

    fun shutdownUserStream() {
        shutdownUserStreamUseCase(Unit)
    }

    fun updateLocalTimeline() {
        val id = localStatuses.value?.get(0)?.id
        if (id == null) {
            error.value = NullPointerException()
        } else {
            val range = Range(sinceId = id)
            getLocalPublicUseCase(range) {
                when (it) {
                    is Success -> localStatuses.insertValues(it.value)
                    is Failure -> error.value = it.reason
                }
            }
        }
    }

    fun postFavorite(target: Status) {
        postFavoriteUseCase(target.id) {
            when (it) {
                is Success -> {
                    homeStatuses.replaceValue(target, it.value)
                    localStatuses.replaceValue(target, it.value)
                    favoritedStatus.value = it.value
                }
                is Failure -> error.value = it.reason
            }
        }
    }

    fun postUnfavorite(target: Status) {
        postUnfavoriteUseCase(target.id) {
            when (it) {
                is Success -> {
                    homeStatuses.replaceValue(target, it.value)
                    localStatuses.replaceValue(target, it.value)
                    unfavoriteStatus.value = it.value
                }
                is Failure -> error.value = it.reason
            }
        }
    }

    fun postReblog(target: Status) {
        postReblogUseCase(target.id) {
            when (it) {
                is Success -> {
                    homeStatuses.replaceValue(target, it.value)
                    localStatuses.replaceValue(target, it.value)
                    rebloggedStatus.value = it.value
                }
                is Failure -> error.value = it.reason
            }
        }
    }
}