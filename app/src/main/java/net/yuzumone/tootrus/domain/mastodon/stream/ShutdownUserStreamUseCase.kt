package net.yuzumone.tootrus.domain.mastodon.stream

import net.yuzumone.tootrus.data.mastodon.StreamRepository
import net.yuzumone.tootrus.domain.UseCase
import javax.inject.Inject

class ShutdownUserStreamUseCase @Inject constructor(
    private val repository: StreamRepository
) : UseCase<Unit, Unit>() {
    override suspend fun run(params: Unit) = repository.shutdown()
}
