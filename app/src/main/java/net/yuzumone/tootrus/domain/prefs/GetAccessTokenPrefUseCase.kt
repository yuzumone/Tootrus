package net.yuzumone.tootrus.domain.prefs

import net.yuzumone.tootrus.data.prefs.PreferenceStorage
import net.yuzumone.tootrus.domain.UseCase
import javax.inject.Inject

class GetAccessTokenPrefUseCase @Inject constructor(
    private val preferenceStorage: PreferenceStorage
) : UseCase<Unit, String?>() {
    override suspend fun run(params: Unit) = preferenceStorage.accessToken
}
