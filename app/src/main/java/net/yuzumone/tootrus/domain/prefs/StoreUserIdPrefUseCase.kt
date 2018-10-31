package net.yuzumone.tootrus.domain.prefs

import net.yuzumone.tootrus.data.prefs.PreferenceStorage
import net.yuzumone.tootrus.domain.UseCase
import javax.inject.Inject

class StoreUserIdPrefUseCase @Inject constructor(
        private val preferenceStorage: PreferenceStorage
) : UseCase<Long, Unit>() {
    override suspend fun run(params: Long) {
        preferenceStorage.userId = params
    }
}