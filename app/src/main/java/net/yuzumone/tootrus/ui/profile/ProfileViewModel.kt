package net.yuzumone.tootrus.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sys1yagi.mastodon4j.api.Range
import com.sys1yagi.mastodon4j.api.entity.Account
import com.sys1yagi.mastodon4j.api.entity.Relationship
import com.sys1yagi.mastodon4j.api.entity.Status
import net.yuzumone.tootrus.domain.Failure
import net.yuzumone.tootrus.domain.Success
import net.yuzumone.tootrus.domain.mastodon.account.*
import net.yuzumone.tootrus.domain.mastodon.status.PostFavoriteUseCase
import net.yuzumone.tootrus.domain.mastodon.status.PostReblogUseCase
import net.yuzumone.tootrus.domain.mastodon.status.PostUnfavoriteUseCase
import net.yuzumone.tootrus.domain.prefs.GetUserIdPrefUseCase
import net.yuzumone.tootrus.ui.common.OnStatusAdapterClickListener
import net.yuzumone.tootrus.util.replaceStatus
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
        private val getAccountAndRelationShipsUseCase: GetAccountAndRelationShipUseCase,
        private val getStatusesUseCase: GetStatusesUseCase,
        private val getFollowingUseCase: GetFollowingUseCase,
        private val getFollowersUseCase: GetFollowersUseCase,
        private val postFollowUseCase: PostFollowUseCase,
        private val postUnFollowUseCase: PostUnFollowUseCase,
        private val postFavoriteUseCase: PostFavoriteUseCase,
        private val postUnfavoriteUseCase: PostUnfavoriteUseCase,
        private val postReblogUseCase: PostReblogUseCase,
        getUserIdPrefUseCase: GetUserIdPrefUseCase
) : ViewModel(), OnStatusAdapterClickListener {

    val userId = MutableLiveData<Long>()
    val account = MutableLiveData<Account>()
    val relationship = MutableLiveData<Relationship>()
    val accountAndRelationship = MutableLiveData<Pair<Account, Relationship>>()
    val statuses = MutableLiveData<List<Status>>()
    val followings = MutableLiveData<List<Account>>()
    val followers = MutableLiveData<List<Account>>()
    val detailActionEvent = MutableLiveData<Status>()
    val replyActionEvent = MutableLiveData<Status>()
    val favoriteActionEvent = MutableLiveData<Status>()
    val unfavoriteActionEvent = MutableLiveData<Status>()
    val reblogActionEvent = MutableLiveData<Status>()
    val menuActionEvent = MutableLiveData<Status>()
    val error = MutableLiveData<Exception>()

    init {
        getUserIdPrefUseCase(Unit) {
            when (it) {
                is Success -> userId.value = it.value
            }
        }
    }

    fun getAccountAndRelationShip(id: Long) {
        getAccountAndRelationShipsUseCase(id) {
            when (it) {
                is Success -> {
                    account.value = it.value.first
                    relationship.value = it.value.second
                    accountAndRelationship.value = it.value
                }
                is Failure -> error.value = it.reason
            }
        }
    }

    fun getStatuses(
            id: Long,
            onlyMedia: Boolean = false,
            excludeReplies: Boolean = false,
            pinned: Boolean = false,
            range: Range = Range()
    ) {
        val params = Params(id, onlyMedia, excludeReplies, pinned, range)
        getStatusesUseCase(params) {
            when (it) {
                is Success -> statuses.value = it.value
                is Failure -> error.value = it.reason
            }
        }
    }

    fun getFollowing(id: Long, range: Range = Range()) {
        getFollowingUseCase(Pair(id, range)) {
            when (it) {
                is Success -> followings.value = it.value
                is Failure -> error.value = it.reason
            }
        }
    }

    fun getFollowers(id: Long, range: Range = Range()) {
        getFollowersUseCase(Pair(id, range)) {
            when (it) {
                is Success -> followers.value = it.value
                is Failure -> error.value = it.reason
            }
        }
    }

    private fun postFollow(id: Long) {
        postFollowUseCase(id) {
            when (it) {
                is Success -> relationship.value = it.value
                is Failure -> error.value = it.reason
            }
        }
    }

    private fun postUnFollow(id: Long) {
        postUnFollowUseCase(id) {
            when (it) {
                is Success -> relationship.value = it.value
                is Failure -> error.value = it.reason
            }
        }
    }

    private fun postFavorite(target: Status) {
        postFavoriteUseCase(target.id) {
            when (it) {
                is Success -> {
                    statuses.replaceStatus(target, it.value)
                    favoriteActionEvent.value = it.value
                }
                is Failure -> error.value = it.reason
            }
        }
    }

    private fun postUnfavorite(target: Status) {
        postUnfavoriteUseCase(target.id) {
            when (it) {
                is Success -> {
                    statuses.replaceStatus(target, it.value)
                    unfavoriteActionEvent.value = it.value
                }
                is Failure -> error.value = it.reason
            }
        }
    }

    private fun postReblog(target: Status) {
        postReblogUseCase(target.id) {
            when (it) {
                is Success -> {
                    statuses.replaceStatus(target, it.value)
                    reblogActionEvent.value = it.value
                }
                is Failure -> error.value = it.reason
            }
        }
    }

    fun actionFollow(isFollow: Boolean) {
        account.value?.let {
            if (isFollow) {
                postUnFollow(it.id)
            } else {
                postFollow(it.id)
            }
        }
    }

    override fun actionDetail(status: Status) {
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
}