package net.yuzumone.tootrus.domain.mastodon.timeline

import com.sys1yagi.mastodon4j.api.Range
import com.sys1yagi.mastodon4j.api.entity.Status
import net.yuzumone.tootrus.data.mastodon.TimelineRepository
import net.yuzumone.tootrus.domain.UseCase
import javax.inject.Inject

class GetTimelineUseCase @Inject constructor(
        private val repository: TimelineRepository
) : UseCase<Range, List<Status>>() {
    override suspend fun run(params: Range) =
            repository.getTimeline(params)
}