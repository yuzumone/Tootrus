package net.yuzumone.tootrus.domain.prefs

import net.yuzumone.tootrus.data.prefs.PreferenceStorage
import net.yuzumone.tootrus.domain.UseCase
import javax.inject.Inject

class StoreAccessTokenPrefUseCase @Inject constructor(
        private val preferenceStorage: PreferenceStorage
) : UseCase<String, Unit>() {
    override suspend fun run(params: String) {
        preferenceStorage.accessToken = params
    }
}