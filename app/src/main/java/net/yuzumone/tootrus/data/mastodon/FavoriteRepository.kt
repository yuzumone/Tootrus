package net.yuzumone.tootrus.data.mastodon

import com.sys1yagi.mastodon4j.MastodonClient
import com.sys1yagi.mastodon4j.api.Range
import com.sys1yagi.mastodon4j.api.entity.Status
import com.sys1yagi.mastodon4j.api.method.Favourites
import net.yuzumone.tootrus.di.InterceptorMastodonClient
import javax.inject.Inject

interface FavoriteRepository {
    fun getFavorites(range: Range): List<Status>
}

class DefaultFavoriteRepository @Inject constructor(
    @InterceptorMastodonClient private val client: MastodonClient
) : FavoriteRepository {
    override fun getFavorites(range: Range): List<Status> =
        Favourites(client).getFavourites(range).execute().part
}
