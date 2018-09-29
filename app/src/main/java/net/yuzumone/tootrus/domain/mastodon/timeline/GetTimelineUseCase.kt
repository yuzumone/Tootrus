package net.yuzumone.tootrus.domain.mastodon.timeline

import com.sys1yagi.mastodon4j.api.Range
import net.yuzumone.tootrus.data.mastodon.TimelineRepository
import net.yuzumone.tootrus.domain.UseCase
import net.yuzumone.tootrus.vo.TootrusStatus
import javax.inject.Inject

class GetTimelineUseCase @Inject constructor(
        private val repository: TimelineRepository
) : UseCase<Range, List<TootrusStatus>>() {
    override suspend fun run(params: Range) =
            repository.getTimeline(params)
}