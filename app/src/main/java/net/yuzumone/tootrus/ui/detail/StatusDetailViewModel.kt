package net.yuzumone.tootrus.ui.detail

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.sys1yagi.mastodon4j.api.entity.Status
import net.yuzumone.tootrus.domain.Failure
import net.yuzumone.tootrus.domain.Success
import net.yuzumone.tootrus.domain.mastodon.status.GetStatusUseCase
import javax.inject.Inject

class StatusDetailViewModel @Inject constructor(
        private val getStatusUseCase: GetStatusUseCase
) : ViewModel() {

    val status = MutableLiveData<Status>()
    val error = MutableLiveData<Exception>()

    fun getStatus(id: Long) {
        getStatusUseCase(id) {
            when (it) {
                is Success -> status.value = it.value
                is Failure -> error.value = it.reason
            }
        }
    }
}