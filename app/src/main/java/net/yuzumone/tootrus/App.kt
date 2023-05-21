package net.yuzumone.tootrus

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Fresco.initialize(this)
    }
}
