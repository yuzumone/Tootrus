package net.yuzumone.tootrus.ui.conversation

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sys1yagi.mastodon4j.api.entity.Status
import net.yuzumone.tootrus.domain.Failure
import net.yuzumone.tootrus.domain.Success
import net.yuzumone.tootrus.domain.mastodon.status.GetConversationUseCase
import net.yuzumone.tootrus.ui.common.OnStatusAdapterSingleClickListener
import javax.inject.Inject

class ConversationViewModel @Inject constructor(
        private val getConversationUseCase: GetConversationUseCase
) : ViewModel(), OnStatusAdapterSingleClickListener {

    val conversations = MutableLiveData<List<Status>>()
    val error = MutableLiveData<Exception>()
    val eventNavigationClick = MutableLiveData<Unit>()
    val eventOpenStatus = MutableLiveData<Status>()

    fun getConversations(status: Status) {
        getConversationUseCase(status) {
            when (it) {
                is Success -> conversations.value = it.value
                is Failure -> error.value = it.reason
            }
        }
    }

    fun onNavigationClick(): View.OnClickListener {
        return View.OnClickListener {
            eventNavigationClick.value = Unit
        }
    }

    override fun onClick(view: View, status: Status) {
        eventOpenStatus.value = status
    }
}