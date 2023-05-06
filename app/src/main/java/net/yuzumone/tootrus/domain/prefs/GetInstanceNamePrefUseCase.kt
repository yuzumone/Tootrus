package net.yuzumone.tootrus.domain.prefs

import net.yuzumone.tootrus.data.prefs.PreferenceStorage
import net.yuzumone.tootrus.domain.UseCase
import javax.inject.Inject

class GetInstanceNamePrefUseCase @Inject constructor(
    private val preferenceStorage: PreferenceStorage
) : UseCase<Unit, String?>() {
    override suspend fun run(params: Unit) = preferenceStorage.instanceName
}
