package net.yuzumone.tootrus.ui.favorite

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sys1yagi.mastodon4j.api.Range
import com.sys1yagi.mastodon4j.api.entity.Status
import net.yuzumone.tootrus.domain.Failure
import net.yuzumone.tootrus.domain.Success
import net.yuzumone.tootrus.domain.mastodon.favorite.GetFavoritesUseCase
import net.yuzumone.tootrus.ui.common.OnStatusAdapterSingleClickListener
import javax.inject.Inject

class FavoriteViewModel @Inject constructor(
        getFavoriteUseCase: GetFavoritesUseCase
) : ViewModel(), OnStatusAdapterSingleClickListener {

    val favorites = MutableLiveData<List<Status>>()
    val error = MutableLiveData<Exception>()
    val eventOpenStatus = MutableLiveData<Status>()

    init {
        getFavoriteUseCase(Range()) {
            when (it) {
                is Success -> favorites.value = it.value
                is Failure -> error.value = it.reason
            }
        }
    }

    override fun onClick(view: View, status: Status) {
        eventOpenStatus.value = status
    }
}