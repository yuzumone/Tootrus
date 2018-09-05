package net.yuzumone.tootrus.ui.top

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.sys1yagi.mastodon4j.api.Handler
import com.sys1yagi.mastodon4j.api.Range
import com.sys1yagi.mastodon4j.api.entity.Notification
import com.sys1yagi.mastodon4j.api.entity.Status
import net.yuzumone.tootrus.domain.Failure
import net.yuzumone.tootrus.domain.Success
import net.yuzumone.tootrus.domain.mastodon.stream.ShutdownUserStreamUseCase
import net.yuzumone.tootrus.domain.mastodon.stream.StartUserStreamUseCase
import net.yuzumone.tootrus.domain.mastodon.timeline.GetTimelineUseCase
import net.yuzumone.tootrus.util.postInsertValue
import javax.inject.Inject

class TopViewModel @Inject constructor(
        private val startUserStreamUseCase: StartUserStreamUseCase,
        private val shutdownUserStreamUseCase: ShutdownUserStreamUseCase,
        getTimelineUseCase: GetTimelineUseCase
): ViewModel() {

    val statuses = MutableLiveData<List<Status>>()
    val error = MutableLiveData<Exception>()

    init {
        val range = Range()
        getTimelineUseCase(range) {
            when (it) {
                is Success -> statuses.value = it.value
                is Failure -> error.value = it.reason
            }
        }
    }

    fun startUserStream() {
        val handler = object : Handler {
            override fun onStatus(status: Status) {
                statuses.postInsertValue(status)
            }

            override fun onNotification(notification: Notification) {

            }

            override fun onDelete(id: Long) {

            }
        }
        startUserStreamUseCase(handler)
    }

    fun shutdownUserStream() {
        shutdownUserStreamUseCase(Unit)
    }
}