package net.yuzumone.tootrus.domain.mastodon.status

import net.yuzumone.tootrus.data.mastodon.StatusRepository
import net.yuzumone.tootrus.domain.UseCase
import net.yuzumone.tootrus.vo.TootrusStatus
import javax.inject.Inject

class PostUnfavoriteUseCase @Inject constructor(
        private val repository: StatusRepository
) : UseCase<Long, TootrusStatus>() {
    override suspend fun run(params: Long) = repository.postUnfavorite(params)
}