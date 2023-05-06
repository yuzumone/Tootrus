package net.yuzumone.tootrus.domain.mastodon.stream

import com.sys1yagi.mastodon4j.api.Handler
import net.yuzumone.tootrus.data.mastodon.StreamRepository
import net.yuzumone.tootrus.domain.UseCase
import javax.inject.Inject

class StartUserStreamUseCase @Inject constructor(
    private val repository: StreamRepository
) : UseCase<Handler, Unit>() {
    override suspend fun run(params: Handler) = repository.user(params)
}
