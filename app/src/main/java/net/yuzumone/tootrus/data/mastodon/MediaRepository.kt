package net.yuzumone.tootrus.data.mastodon

import com.sys1yagi.mastodon4j.MastodonClient
import com.sys1yagi.mastodon4j.api.entity.Attachment
import com.sys1yagi.mastodon4j.api.method.Media
import net.yuzumone.tootrus.di.InterceptorMastodonClient
import okhttp3.MultipartBody
import javax.inject.Inject

interface MediaRepository {
    fun postMedia(files: List<MultipartBody.Part>): List<Attachment>
}

class DefaultMediaRepository @Inject constructor(
    @InterceptorMastodonClient private val client: MastodonClient
) : MediaRepository {
    override fun postMedia(files: List<MultipartBody.Part>): List<Attachment> {
        val result = ArrayList<Attachment>()
        files.forEach {
            result.add(Media(client).postMedia(it).execute())
        }
        return result
    }
}
