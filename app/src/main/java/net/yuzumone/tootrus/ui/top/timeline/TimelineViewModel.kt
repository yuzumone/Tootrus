package net.yuzumone.tootrus.ui.top.timeline

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.sys1yagi.mastodon4j.api.Range
import com.sys1yagi.mastodon4j.api.entity.Status
import net.yuzumone.tootrus.domain.Failure
import net.yuzumone.tootrus.domain.Success
import net.yuzumone.tootrus.domain.mastodon.timeline.GetTimelineUseCase
import javax.inject.Inject

class TimelineViewModel @Inject constructor(
        private val getTimelineUseCase: GetTimelineUseCase
) : ViewModel() {

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
}