package net.yuzumone.tootrus.ui.profile

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.sys1yagi.mastodon4j.api.entity.Account
import com.sys1yagi.mastodon4j.api.entity.Relationship
import net.yuzumone.tootrus.domain.Failure
import net.yuzumone.tootrus.domain.Success
import net.yuzumone.tootrus.domain.mastodon.account.GetAccountAndRelationShipUseCase
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
        private val getAccountAndRelationShipsUseCase: GetAccountAndRelationShipUseCase
) : ViewModel() {

    val account = MutableLiveData<Account>()
    val relationship = MutableLiveData<Relationship>()
    val error = MutableLiveData<Exception>()

    fun getAccountAndRelationShip(id: Long) {
        getAccountAndRelationShipsUseCase(id) {
            when (it) {
                is Success -> {
                    account.value = it.value.first
                    relationship.value = it.value.second
                }
                is Failure -> error.value = it.reason
            }
        }
    }
}