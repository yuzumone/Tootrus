package net.yuzumone.tootrus.domain.mastodon.timeline

import com.sys1yagi.mastodon4j.api.Range
import net.yuzumone.tootrus.data.mastodon.PublicRepository
import net.yuzumone.tootrus.domain.UseCase
import net.yuzumone.tootrus.vo.TootrusStatus
import javax.inject.Inject

class GetLocalPublicUseCase @Inject constructor(
        private val repository: PublicRepository
) : UseCase<Range, List<TootrusStatus>>() {
    override suspend fun run(params: Range) = repository.getLocalPublic(params)
}