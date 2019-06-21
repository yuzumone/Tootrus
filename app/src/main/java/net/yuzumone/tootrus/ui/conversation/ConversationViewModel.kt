package net.yuzumone.tootrus.ui.conversation

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sys1yagi.mastodon4j.api.entity.Status
import net.yuzumone.tootrus.domain.Failure
import net.yuzumone.tootrus.domain.Success
import net.yuzumone.tootrus.domain.mastodon.status.GetConversationUseCase
import net.yuzumone.tootrus.ui.common.OnStatusAdapterClickListener
import javax.inject.Inject

class ConversationViewModel @Inject constructor(
        private val getConversationUseCase: GetConversationUseCase
) : ViewModel(), OnStatusAdapterClickListener {

    val conversations = MutableLiveData<List<Status>>()
    val error = MutableLiveData<Exception>()
    val eventNavigationClick = MutableLiveData<Unit>()

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

    override fun actionDetail(status: Status) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun actionReply(status: Status) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun actionFavorite(status: Status) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun actionReblog(status: Status) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun actionMenu(status: Status) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}