package net.yuzumone.tootrus.domain.mastodon.status

import com.sys1yagi.mastodon4j.api.entity.Status
import net.yuzumone.tootrus.data.mastodon.StatusRepository
import net.yuzumone.tootrus.domain.UseCase
import javax.inject.Inject

class PostStatusUseCase @Inject constructor(
        private val repository: StatusRepository
) : UseCase<PostStatusParams, Status>() {
    override suspend fun run(params: PostStatusParams) =
            repository.postStatus(
                    params.status,
                    params.inReplyToId,
                    params.mediaIds,
                    params.sensitive,
                    params.spoilerText,
                    params.visibility)
}

data class PostStatusParams(
        val status: String,
        val inReplyToId: Long?,
        val mediaIds: List<Long>?,
        val sensitive: Boolean,
        val spoilerText: String?,
        val visibility: Status.Visibility
)