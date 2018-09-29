package net.yuzumone.tootrus.vo

import com.sys1yagi.mastodon4j.api.entity.*
import java.io.Serializable

data class TootrusStatus(
        val id: Long = 0L,
        val uri: String = "",
        val url: String? = "",
        val account: Account? = null,
        val inReplyToId: Long? = null,
        val inReplyToAccountId: Long? = null,
        var reblog: TootrusStatus? = null,
        var hasReblog: Boolean = false,
        val content: String = "",
        val createdAt: String = "",
        val emojis: List<Emoji> = emptyList(),
        val repliesCount: Int = 0,
        val reblogsCount: Int = 0,
        val favoritesCount: Int = 0,
        var isReblogged: Boolean = false,
        var isFavorited: Boolean = false,
        val isSensitive: Boolean = false,
        val spoilerText: String = "",
        val visibility: String = Visibility.Public.value,
        val mediaAttachments: List<Attachment> = emptyList(),
        val mentions: List<Mention> = emptyList(),
        val tags: List<Tag> = emptyList(),
        val application: Application? = null,
        val language: String? = "",
        val pinned: Boolean? = null
) : Serializable {

    constructor(status: com.sys1yagi.mastodon4j.api.entity.Status) : this (
            id = status.id,
            uri = status.uri,
            url = status.url,
            account = status.account,
            inReplyToId = status.inReplyToId,
            inReplyToAccountId = status.inReplyToAccountId,
            content = status.content,
            createdAt = status.createdAt,
            emojis = status.emojis,
            repliesCount = status.repliesCount,
            reblogsCount = status.reblogsCount,
            favoritesCount = status.favouritesCount,
            isReblogged = status.isReblogged,
            isFavorited = status.isFavourited,
            isSensitive = status.isSensitive,
            spoilerText = status.spoilerText,
            visibility = status.visibility,
            mediaAttachments = status.mediaAttachments,
            mentions = status.mentions,
            tags = status.tags,
            application = status.application,
            language = status.language,
            pinned = status.pinned
    ) {
        if (status.reblog != null) {
            this.reblog = TootrusStatus(status.reblog!!)
            this.hasReblog = true
        }
    }

    enum class Visibility(val value: String) {
        Public("public"),
        Unlisted("unlisted"),
        Private("private"),
        Direct("direct")
    }
}