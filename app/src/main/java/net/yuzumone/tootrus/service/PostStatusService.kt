package net.yuzumone.tootrus.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.widget.Toast
import dagger.android.AndroidInjection
import net.yuzumone.tootrus.R
import net.yuzumone.tootrus.domain.Failure
import net.yuzumone.tootrus.domain.Success
import net.yuzumone.tootrus.domain.mastodon.status.PostStatusParams
import net.yuzumone.tootrus.domain.mastodon.status.PostStatusUseCase
import javax.inject.Inject

class PostStatusService : Service() {

    @Inject lateinit var postStatusUseCase: PostStatusUseCase

    companion object {
        private const val ARG_POST_STATUS_PARAMS = "post_status_params"
        fun createIntent(context: Context, params: PostStatusParams): Intent {
            return Intent(context, PostStatusService::class.java).apply {
                putExtra(ARG_POST_STATUS_PARAMS, params)
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.getSerializableExtra(ARG_POST_STATUS_PARAMS)?.let {
            val params = it as PostStatusParams
            postStatusUseCase(params) { result ->
                when (result) {
                    is Success -> {
                        showToast(R.string.toot)
                        stopSelf()
                    }
                    is Failure -> {
                        showToast(R.string.error)
                        stopSelf()
                    }
                }
            }
        }
        startForeground(1, createNotification())
        return START_NOT_STICKY
    }

    private fun createNotification(): Notification {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val id = getString(R.string.channel_id)
        val name = getString(R.string.channel_name)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(id, name, importance)
            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            channel.enableVibration(true)
            channel.enableLights(true)
            manager.createNotificationChannel(channel)
        }
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, id)
                .setSmallIcon(android.R.drawable.ic_menu_send)
                .setContentText(getString(R.string.toot_now))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
        return notificationBuilder.build()
    }

    private fun showToast(resId: Int) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
    }
}