package net.yuzumone.tootrus.domain.mastodon.timeline

import com.sys1yagi.mastodon4j.api.Range
import com.sys1yagi.mastodon4j.api.entity.Status
import net.yuzumone.tootrus.data.mastodon.PublicRepository
import net.yuzumone.tootrus.domain.UseCase
import javax.inject.Inject

class GetLocalPublicUseCase @Inject constructor(
        private val repository: PublicRepository
) : UseCase<Range, List<Status>>() {
    override suspend fun run(params: Range) = repository.getLocalPublic(params)
}