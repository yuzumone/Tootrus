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
import net.yuzumone.tootrus.domain.mastodon.stream.ShutdownUserStreamUseCase
import net.yuzumone.tootrus.domain.mastodon.stream.StartUserStreamUseCase
import net.yuzumone.tootrus.domain.mastodon.timeline.GetLocalPublicUseCase
import net.yuzumone.tootrus.domain.mastodon.timeline.GetTimelineUseCase
import net.yuzumone.tootrus.util.insertValues
import net.yuzumone.tootrus.util.postInsertValue
import net.yuzumone.tootrus.vo.TootrusNotification
import net.yuzumone.tootrus.vo.TootrusStatus
import javax.inject.Inject

class TopViewModel @Inject constructor(
        private val startUserStreamUseCase: StartUserStreamUseCase,
        private val shutdownUserStreamUseCase: ShutdownUserStreamUseCase,
        private val postFavoriteUseCase: PostFavoriteUseCase,
        private val getLocalPublicUseCase: GetLocalPublicUseCase,
        getTimelineUseCase: GetTimelineUseCase,
        getNotificationsUseCase: GetNotificationsUseCase
): ViewModel() {

    val homeStatuses = MutableLiveData<List<TootrusStatus>>()
    val localStatuses = MutableLiveData<List<TootrusStatus>>()
    val favoritedStatus = MutableLiveData<TootrusStatus>()
    val notifications = MutableLiveData<List<TootrusNotification>>()
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
                homeStatuses.postInsertValue(TootrusStatus(status))
            }

            override fun onNotification(notification: Notification) {
                notifications.postInsertValue(TootrusNotification(notification))
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

    fun postFavorite(target: TootrusStatus) {
        postFavoriteUseCase(target.id) {
            when (it) {
                is Success -> {
                    changeIsFavoriteValue(target, true)
                    favoritedStatus.value = it.value
                }
                is Failure -> error.value = it.reason
            }
        }
    }

    private fun changeIsFavoriteValue(target: TootrusStatus, state: Boolean) {
        val homeArray = arrayListOf<TootrusStatus>()
        homeStatuses.value?.forEach {
            if (it.id == target.id) {
                it.isFavorited = state
            }
            homeArray.add(it)
        }
        homeStatuses.postValue(homeArray)
        val localArray = arrayListOf<TootrusStatus>()
        localStatuses.value?.forEach {
            if (it.id == target.id) {
                it.isFavorited = state
            }
            localArray.add(it)
        }
        localStatuses.postValue(localArray)
    }
}