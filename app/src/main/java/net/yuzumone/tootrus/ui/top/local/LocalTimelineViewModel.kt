package net.yuzumone.tootrus.ui.top.local

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.sys1yagi.mastodon4j.api.Range
import com.sys1yagi.mastodon4j.api.entity.Status
import net.yuzumone.tootrus.domain.Failure
import net.yuzumone.tootrus.domain.Success
import net.yuzumone.tootrus.domain.mastodon.status.PostFavoriteUseCase
import net.yuzumone.tootrus.domain.mastodon.timeline.GetLocalPublicUseCase
import net.yuzumone.tootrus.util.insertValues
import javax.inject.Inject

class LocalTimelineViewModel @Inject constructor(
        private val getLocalPublicUseCase: GetLocalPublicUseCase,
        private val postFavoriteUseCase: PostFavoriteUseCase
) : ViewModel() {

    val statuses = MutableLiveData<List<Status>>()
    val favoritedStatus = MutableLiveData<Status>()
    val error = MutableLiveData<Exception>()

    init {
        getLocalPublicUseCase(Range()) {
            when (it) {
                is Success -> statuses.value = it.value
                is Failure -> error.value = it.reason
            }
        }
    }

    fun updateLocalTimeline() {
        val id = statuses.value?.get(0)?.id
        if (id == null) {
            error.value = NullPointerException()
        } else {
            val range = Range(sinceId = id)
            getLocalPublicUseCase(range) {
                when (it) {
                    is Success -> statuses.insertValues(it.value)
                    is Failure -> error.value = it.reason
                }
            }
        }
    }

    fun postFavorite(target: Status) {
        postFavoriteUseCase(target.id) {
            when (it) {
                is Success -> favoritedStatus.value = it.value
                is Failure -> error.value = it.reason
            }
        }
    }
}