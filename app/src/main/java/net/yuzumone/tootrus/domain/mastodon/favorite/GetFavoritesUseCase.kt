package net.yuzumone.tootrus.domain.mastodon.favorite

import com.sys1yagi.mastodon4j.api.Range
import com.sys1yagi.mastodon4j.api.entity.Status
import net.yuzumone.tootrus.data.mastodon.FavoriteRepository
import net.yuzumone.tootrus.domain.UseCase
import javax.inject.Inject

class GetFavoritesUseCase @Inject constructor(
    private val repository: FavoriteRepository
) : UseCase<Range, List<Status>>() {
    override suspend fun run(params: Range): List<Status> =
        repository.getFavorites(params)
}
