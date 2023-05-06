package net.yuzumone.tootrus.domain.mastodon.status

import com.sys1yagi.mastodon4j.api.entity.Status
import net.yuzumone.tootrus.data.mastodon.StatusRepository
import net.yuzumone.tootrus.domain.UseCase
import javax.inject.Inject

class PostFavoriteUseCase @Inject constructor(
    private val repository: StatusRepository
) : UseCase<Long, Status>() {
    override suspend fun run(params: Long) = repository.postFavorite(params)
}
