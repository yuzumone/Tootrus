package net.yuzumone.tootrus.data.mastodon

import com.sys1yagi.mastodon4j.MastodonClient
import com.sys1yagi.mastodon4j.api.Range
import com.sys1yagi.mastodon4j.api.entity.Account
import com.sys1yagi.mastodon4j.api.entity.Relationship
import com.sys1yagi.mastodon4j.api.entity.Status
import com.sys1yagi.mastodon4j.api.method.Accounts
import javax.inject.Inject
import javax.inject.Named

interface AccountRepository {
    fun getAccount(id: Long): Account
    fun getVerifyCredentials(): Account
    fun getRelationShips(ids: List<Long>): List<Relationship>
    fun getStatuses(
        id: Long,
        onlyMedia: Boolean,
        excludeReplies: Boolean,
        pinned: Boolean,
        range: Range
    ): List<Status>

    fun getFollowers(id: Long, range: Range): List<Account>
    fun getFollowing(id: Long, range: Range): List<Account>
    fun postFollow(id: Long): Relationship
    fun postUnFollow(id: Long): Relationship
}

class DefaultAccountRepository @Inject constructor(
    @Named("client") private val client: MastodonClient
) : AccountRepository {
    override fun getAccount(id: Long): Account {
        return Accounts(client).getAccount(id).execute()
    }

    override fun getVerifyCredentials(): Account {
        return Accounts(client).getVerifyCredentials().execute()
    }

    override fun getRelationShips(ids: List<Long>): List<Relationship> {
        return Accounts(client).getRelationships(ids).execute()
    }

    override fun getStatuses(
        id: Long,
        onlyMedia: Boolean,
        excludeReplies: Boolean,
        pinned: Boolean,
        range: Range
    ): List<Status> {
        return Accounts(client).getStatuses(id, onlyMedia, excludeReplies, pinned, range)
            .execute().part
    }

    override fun getFollowers(id: Long, range: Range): List<Account> {
        return Accounts(client).getFollowers(id, range).execute().part
    }

    override fun getFollowing(id: Long, range: Range): List<Account> {
        return Accounts(client).getFollowing(id, range).execute().part
    }

    override fun postFollow(id: Long): Relationship {
        return Accounts(client).postFollow(id).execute()
    }

    override fun postUnFollow(id: Long): Relationship {
        return Accounts(client).postUnFollow(id).execute()
    }
}
