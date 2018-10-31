package net.yuzumone.tootrus.ui.profile

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.sys1yagi.mastodon4j.api.Range
import com.sys1yagi.mastodon4j.api.entity.Account
import com.sys1yagi.mastodon4j.api.entity.Relationship
import com.sys1yagi.mastodon4j.api.entity.Status
import net.yuzumone.tootrus.domain.Failure
import net.yuzumone.tootrus.domain.Success
import net.yuzumone.tootrus.domain.mastodon.account.*
import net.yuzumone.tootrus.domain.prefs.GetUserIdPrefUseCase
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
        private val getAccountAndRelationShipsUseCase: GetAccountAndRelationShipUseCase,
        private val getStatusesUseCase: GetStatusesUseCase,
        private val getFollowingUseCase: GetFollowingUseCase,
        private val getFollowersUseCase: GetFollowersUseCase,
        private val postFollowUseCase: PostFollowUseCase,
        private val postUnFollowUseCase: PostUnFollowUseCase,
        getUserIdPrefUseCase: GetUserIdPrefUseCase
) : ViewModel() {

    val userId = MutableLiveData<Long>()
    val account = MutableLiveData<Account>()
    val relationship = MutableLiveData<Relationship>()
    val statuses = MutableLiveData<List<Status>>()
    val followings = MutableLiveData<List<Account>>()
    val followers = MutableLiveData<List<Account>>()
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

    fun handleFollowButton(isFollow: Boolean) {
        account.value?.let {
            if (isFollow) {
                postUnFollow(it.id)
            } else {
                postFollow(it.id)
            }
        }
    }

    fun postFollow(id: Long) {
        postFollowUseCase(id) {
            when (it) {
                is Success -> relationship.value = it.value
                is Failure -> error.value = it.reason
            }
        }
    }

    fun postUnFollow(id: Long) {
        postUnFollowUseCase(id) {
            when (it) {
                is Success -> relationship.value = it.value
                is Failure -> error.value = it.reason
            }
        }
    }
}