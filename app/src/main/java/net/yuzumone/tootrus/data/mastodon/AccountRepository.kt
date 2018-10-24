package net.yuzumone.tootrus.data.mastodon

import com.sys1yagi.mastodon4j.MastodonClient
import com.sys1yagi.mastodon4j.api.entity.Account
import com.sys1yagi.mastodon4j.api.method.Accounts
import javax.inject.Inject
import javax.inject.Named

interface AccountRepository {
    fun getAccount(id: Long): Account
}

class DefaultAccountRepository @Inject constructor(
        @Named("client") private val client: MastodonClient
) : AccountRepository{
    override fun getAccount(id: Long): Account {
        return Accounts(client).getAccount(id).execute()
    }
}