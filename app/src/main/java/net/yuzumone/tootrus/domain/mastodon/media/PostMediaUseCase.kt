package net.yuzumone.tootrus.domain.mastodon.media

import com.sys1yagi.mastodon4j.api.entity.Attachment
import net.yuzumone.tootrus.data.mastodon.MediaRepository
import net.yuzumone.tootrus.domain.UseCase
import okhttp3.MultipartBody
import javax.inject.Inject

class PostMediaUseCase @Inject constructor(
    private val repository: MediaRepository
) : UseCase<List<MultipartBody.Part>, List<Attachment>>() {
    override suspend fun run(params: List<MultipartBody.Part>) =
        repository.postMedia(params)
}
