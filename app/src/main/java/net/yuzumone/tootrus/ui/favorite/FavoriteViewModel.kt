package net.yuzumone.tootrus.ui.favorite

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sys1yagi.mastodon4j.api.Range
import com.sys1yagi.mastodon4j.api.entity.Status
import net.yuzumone.tootrus.domain.Failure
import net.yuzumone.tootrus.domain.Success
import net.yuzumone.tootrus.domain.mastodon.favorite.GetFavoritesUseCase
import net.yuzumone.tootrus.ui.common.OnStatusAdapterClickListener
import javax.inject.Inject

class FavoriteViewModel @Inject constructor(
        getFavoriteUseCase: GetFavoritesUseCase
) : ViewModel(), OnStatusAdapterClickListener {

    val favorites = MutableLiveData<List<Status>>()
    val error = MutableLiveData<Exception>()

    init {
        getFavoriteUseCase(Range()) {
            when (it) {
                is Success -> favorites.value = it.value
                is Failure -> error.value = it.reason
            }
        }
    }

    override fun actionDetail(status: Status) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun actionReply(status: Status) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun actionFavorite(status: Status) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun actionReblog(status: Status) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun actionMenu(status: Status) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}