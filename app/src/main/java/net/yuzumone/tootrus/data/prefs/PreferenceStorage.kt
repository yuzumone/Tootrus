package net.yuzumone.tootrus.data.prefs

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import javax.inject.Inject
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

interface PreferenceStorage {
    var accessToken: String?
}

class SharedPreferenceStorage @Inject constructor(context: Context) : PreferenceStorage {

    private val prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME = "net.yuzumone.tootrus.prefs"
        private const val PREF_ACCESS_TOKEN = "pref_access_token"
    }

    override var accessToken by StringPreference(prefs, PREF_ACCESS_TOKEN, null)
}

class StringPreference(
        private val pref: SharedPreferences,
        private val key: String,
        private val defaultValue: String?
) : ReadWriteProperty<Any, String?> {

    override fun getValue(thisRef: Any, property: KProperty<*>): String? {
        return pref.getString(key, defaultValue)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: String?) {
        pref.edit().putString(key, value).apply()
    }
}