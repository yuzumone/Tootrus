package net.yuzumone.tootrus.domain.prefs

import net.yuzumone.tootrus.data.prefs.PreferenceStorage
import net.yuzumone.tootrus.domain.UseCase
import javax.inject.Inject

class StoreInstanceNameAndTokenUseCase @Inject constructor(
        private val preferenceStorage: PreferenceStorage
) : UseCase<Pair<String, String>, Unit>() {
    override suspend fun run(params: Pair<String, String>) {
        preferenceStorage.instanceName = params.first
        preferenceStorage.accessToken = params.second
    }
}